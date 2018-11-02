package org.adobecommunity.site.impl;

import java.io.IOException;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;

import org.adobecommunity.site.SendGridIntegration;
import org.adobecommunity.site.SendGridResponse;
import org.adobecommunity.site.impl.SendGridIntegrationImpl.SendGridConfiguration;
import org.adobecommunity.site.models.InitialUserProfile;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.HttpHeaders;

@Component(service = SendGridIntegration.class)
@Designate(ocd = SendGridConfiguration.class)
public class SendGridIntegrationImpl implements SendGridIntegration {

    @ObjectClassDefinition(name = "Send Grid Configuration")
    public @interface SendGridConfiguration {
        @AttributeDefinition(type = AttributeType.PASSWORD)
        String apiKey();
    }
    private static final String ADD_USER_ENDPOINT = "https://api.sendgrid.com/v3/contactdb/recipients";
    private static final String ADD_LIST_ENDPOINT = "https://api.sendgrid.com/v3/contactdb/lists/%s/recipients/%s";

    private static final Logger log = LoggerFactory.getLogger(SendGridIntegrationImpl.class);

    private SendGridConfiguration config;

    @Activate
    public void activate(SendGridConfiguration config) {
        this.config = config;
    }

    @Override
    public String addToList(String userId, int listId) {
        CloseableHttpResponse apiResponse = null;
        String status = null;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            HttpPost postRequest = new HttpPost(String.format(ADD_LIST_ENDPOINT, listId, userId));

            // set authentication header
            postRequest.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + config.apiKey());
            postRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            apiResponse = httpclient.execute(postRequest);

            log.debug("Received response: {}", apiResponse.getStatusLine());
            String stringResponse = EntityUtils.toString(apiResponse.getEntity());
            log.trace("Recieved response body: {}", stringResponse);

            status =  apiResponse.getStatusLine().toString();

        } catch (IOException e) {
            log.warn("Exception calling SendGrid API", e);
            status = e.getMessage();
        }
        return status;
    }

    @Override
    public SendGridResponse createUser(InitialUserProfile profile) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder().add("company", profile.getCompany())
                .add("email", profile.getEmail()).add("hosting", profile.getHosting())
                .add("membership", profile.getLevel()).add("organizing", profile.getOrganizing())
                .add("phone", profile.getPhone()).add("presentation", profile.getPresentation())
                .add("role", profile.getRole()).add("topics", profile.getTopics())
                .add("products", String.join(",", profile.getProducts()));

        String name = profile.getName();
        String firstName = name;
        String lastName = "";
        if (name.contains(" ")) {
            firstName = name.substring(0, name.indexOf(' '));
            lastName = name.substring(name.indexOf(' '), name.length());
        }
        objectBuilder.add("first_name", firstName);
        objectBuilder.add("last_name", lastName);

        return createUser(objectBuilder.build());
    }

    private SendGridResponse createUser(JsonObject data) {
        CloseableHttpResponse apiResponse = null;
        JsonObject response = null;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            HttpPatch patchRequest = new HttpPatch(ADD_USER_ENDPOINT);

            // set authentication header
            patchRequest.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + config.apiKey());
            patchRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            // build the body
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            arrayBuilder.add(data);
            String json = arrayBuilder.build().toString();
            log.debug("Sending subscription request {}", json);

            patchRequest.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

            apiResponse = httpclient.execute(patchRequest);

            log.debug("Received response: {}", apiResponse.getStatusLine());
            String stringResponse = EntityUtils.toString(apiResponse.getEntity());
            log.trace("Recieved response body: {}", stringResponse);

            try (JsonReader reader = Json.createReader(new StringReader(stringResponse))) {
                response = reader.readObject();
            }

        } catch (IOException e) {
            log.warn("Exception calling SendGrid API", e);
        }
        return new SendGridResponse(response);
    }

    @Override
    public SendGridResponse createUser(String email) {
        JsonObject obj = Json.createObjectBuilder().add("email", email).build();
        return createUser(obj);
    }

}

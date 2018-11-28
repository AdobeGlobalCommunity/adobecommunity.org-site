package org.adobecommunity.site.impl.jobs;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.adobecommunity.site.internal.DispatcherSecurityScanServlet;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = JobConsumer.class, property = {
        JobConsumer.PROPERTY_TOPICS + "=" + AEMDispatcherScanConsumer.TOPIC })
public class AEMDispatcherScanConsumer implements JobConsumer {

    public static final String TOPIC = "adobecommunity-org/scan/aemdispatcher";

    public static final String PARAM_FORM = "form";
    public static final String PARAM_CONFIG = "config";

    private static final Logger log = LoggerFactory.getLogger(AEMDispatcherScanConsumer.class);

    @Reference
    private ResourceResolverFactory rrFactory;

    @Override
    public JobResult process(Job cfg) {
        ResourceResolver resolver = null;
        try {
            resolver = rrFactory.getServiceResourceResolver(new HashMap<String, Object>() {
                private static final long serialVersionUID = 1L;
                {
                    put(ResourceResolverFactory.SUBSERVICE, "ugc");
                }
            });

            Resource form = resolver.getResource(cfg.getProperty(PARAM_FORM, String.class));
            Resource configRsrc = resolver.getResource(cfg.getProperty(PARAM_CONFIG, String.class))
                    .getChild(JcrConstants.JCR_CONTENT);
            ValueMap config = configRsrc.getValueMap();

            ModifiableValueMap mvm = configRsrc.adaptTo(ModifiableValueMap.class);
            mvm.put("status", "Running");
            configRsrc.getResourceResolver().commit();

            final Map<String, String> params = new HashMap<>();
            config.keySet().forEach(k -> {
                if (!k.contains(":")) {
                    params.put(k, config.get(k, String.class));
                }
            });
            params.put("host", config.get(DispatcherSecurityScanServlet.PARAM_PROTOCOL) + "://"
                    + config.get(DispatcherSecurityScanServlet.PARAM_DOMAIN));
            final StrSubstitutor sub = new StrSubstitutor(params);

            Resource checksParent = form.getChild("checks");
            if (checksParent != null) {
                int count = 0;
                int succeeded = 0;
                for (Resource check : checksParent.getChildren()) {
                    configRsrc.getResourceResolver().refresh();
                    mvm = configRsrc.adaptTo(ModifiableValueMap.class);
                    if (executeCheck(check, sub, configRsrc)) {
                        succeeded++;
                        mvm.put("succeeded", succeeded);
                    }
                    count++;

                    mvm.put("count", count);
                    configRsrc.getResourceResolver().commit();
                }
                configRsrc.getResourceResolver().refresh();
                mvm = configRsrc.adaptTo(ModifiableValueMap.class);
                mvm.put("status", "Complete");
                configRsrc.getResourceResolver().commit();
            }

        } catch (LoginException e) {
            log.error("Failed to login", e);
        } catch (PersistenceException e) {
            log.error("Failed to persist status", e);
        } finally {
            if (resolver != null) {
                resolver.close();
            }
        }
        return null;
    }

    private boolean executeCheck(Resource check, StrSubstitutor sub, Resource configRsrc) {
        ValueMap properties = check.getValueMap();
        String url = sub.replace(properties.get("url", String.class));

        boolean succeeded = true;
        String message = "Check " + url + " successful!";

        CloseableHttpResponse response = null;
        String status = null;
        String statusLine = null;
        String responseBody = null;
        String headers = null;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            HttpRequestBase request;

            if ("GET".equals(properties.get("method", "GET"))) {
                log.debug("Sending GET request to {}", url);
                request = new HttpGet(url);
            } else {
                log.debug("Sending POST request to {}", url);
                request = new HttpPost(url);
            }

            response = httpclient.execute(request);

            log.debug("Received response: {}", response.getStatusLine());
            responseBody = EntityUtils.toString(response.getEntity());

            status = String.valueOf(response.getStatusLine().getStatusCode());

            statusLine = response.getStatusLine().toString();

            StringBuilder hsb = new StringBuilder();
            for (Header header : response.getAllHeaders()) {
                hsb.append(header.getName() + ": " + header.getValue());
            }
            headers = hsb.toString();
        } catch (Exception e) {
            log.warn("Exception making request " + url, e);
            succeeded = false;
            message = "Failed to call: " + url + " with message: " + e.getMessage();
            statusLine = e.getMessage();
        }

        // first check the response code
        String[] validResponseCodes = properties.get("responsecodes",
                check.getParent().getParent().getValueMap().get("responsecodes", new String[0]));

        if (succeeded && !ArrayUtils.contains(validResponseCodes, status)) {
            message = url + " returned unexpected response code " + status;
            succeeded = false;
        }

        // next check body
        String bodyPatternStr = properties.get("responsebody", String.class);
        if (succeeded && StringUtils.isNotBlank(bodyPatternStr)) {
            Pattern bodyPattern = Pattern.compile(bodyPatternStr);
            if (bodyPattern.matcher(responseBody).find()) {
                message = url + " returned content containing " + bodyPatternStr;
                succeeded = false;
            }
        }

        // next check the headers
        String headerPatternStr = properties.get("headers", String.class);
        if (succeeded && StringUtils.isNotBlank(headerPatternStr)) {
            Pattern pattern = Pattern.compile(headerPatternStr);
            if (pattern.matcher(headers).find()) {
                message = url + " returned headers containing " + headerPatternStr;
                succeeded = false;
            }
        }

        try {
            Map<String, Object> params = new HashMap<>();
            params.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
            params.put("succeeded", succeeded);
            params.put("message", message);
            params.put("statusline", statusLine);
            params.put("link", url);
            ResourceResolver resolver = configRsrc.getResourceResolver();
            resolver.create(configRsrc, check.getName(), params);
            resolver.commit();
        } catch (PersistenceException e) {
            log.error("Failed to persist check result", e);
        }

        return succeeded;

    }

}

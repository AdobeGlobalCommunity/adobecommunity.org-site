package org.adobecommunity.site;

import javax.json.JsonObject;

import org.apache.jackrabbit.util.Base64;

public class SendGridResponse {

    private final String email;
    private final String id;
    private final boolean successful;
    private final boolean updated;
    private final JsonObject rawResponse;

    public SendGridResponse(JsonObject rawResponse) {
        this.rawResponse = rawResponse;
        this.successful = rawResponse.getInt("error_count") == 0;
        this.updated = rawResponse.getInt("new_count") == 1 || rawResponse.getInt("updated_count") == 1;
        if (successful) {
            email = Base64.decode(rawResponse.getJsonArray("persisted_recipients").getString(0));
            id = rawResponse.getJsonArray("persisted_recipients").getString(0);
        } else {
            email = null;
            id = null;
        }
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public JsonObject getRawResponse() {
        return rawResponse;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public boolean isUpdated() {
        return updated;
    }

    @Override
    public String toString() {
        return "SendGridResponse [email=" + email + ", id=" + id + ", successful=" + successful + ", updated=" + updated
                + ", rawResponse=" + rawResponse + "]";
    }

}

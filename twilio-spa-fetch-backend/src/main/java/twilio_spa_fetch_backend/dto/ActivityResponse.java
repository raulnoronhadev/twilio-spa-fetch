package twilio_spa_fetch_backend.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;
import java.util.Date;
import java.util.Map;

public record ActivityResponse(
        @JsonProperty("account_sid") String accountSid,
        Boolean available,
        @JsonProperty("date_created") Date dateCreated,
        @JsonProperty("date_updated") Date dateUpdated,
        @JsonProperty("friendly_name") String friendlyName,
        String sid,
        URI url,
        @JsonProperty("workspace_sid") String workspaceSid,
        Map<String, String> links
) {}

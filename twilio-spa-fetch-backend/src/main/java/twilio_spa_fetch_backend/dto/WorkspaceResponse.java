package twilio_spa_fetch_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public record WorkspaceResponse(
        @JsonProperty("account_sid") String accountSid,
        String sid,
        @JsonProperty("friendly_name") String friendlyName,
        @JsonProperty("date_created") ZonedDateTime dateCreated,
        @JsonProperty("date_updated") ZonedDateTime dateUpdated,
        @JsonProperty("default_activity_name") String defaultActivityName,
        @JsonProperty("default_activity_sid") String defaultActivitySid
) {}

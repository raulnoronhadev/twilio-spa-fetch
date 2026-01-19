package twilio_spa_fetch_backend.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

public record WorkerRecordDTO(
        @JsonProperty("account_sid") String accountSid,
        @JsonProperty("friendly_name") String friendlyName,
        String sid,
        @JsonProperty("workspace_sid") String workspaceSid,
        @JsonProperty("activity_name") String activityName,
        String attributes,
        Boolean available,
        @JsonProperty("date_created") ZonedDateTime dateCreated,
        @JsonProperty("date_updated") ZonedDateTime dateUpdated
) {}

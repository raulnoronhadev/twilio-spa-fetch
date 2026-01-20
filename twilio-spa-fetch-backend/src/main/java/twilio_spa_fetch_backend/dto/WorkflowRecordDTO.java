package twilio_spa_fetch_backend.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

public record WorkflowRecordDTO(
        @JsonProperty("account_sid") String accountSid,
        String sid,
        @JsonProperty("friendly_name") String friendlyName,
        @JsonProperty("workspace_sid") String workspaceSid,
        String configuration,
        @JsonProperty("date_created") ZonedDateTime dateCreated,
        @JsonProperty("date_updated") ZonedDateTime dateUpdated,
        @JsonProperty("task_reservation_timeout") Integer taskReservationTimeout
) {}

package twilio_spa_fetch_backend.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;
import java.time.ZonedDateTime;

public record WorkflowDTO(
        @JsonProperty("account_sid") String accountSid,
        String sid,
        @JsonProperty("friendly_name") String friendlyName,
        @JsonProperty("workspace_sid") String workspaceSid,
        @JsonProperty("assignment_callback_url") URI assignmentCallbackUrl,
        String configuration,
        @JsonProperty("date_created") ZonedDateTime dateCreated,
        @JsonProperty("date_updated") ZonedDateTime dateUpdated,
        @JsonProperty("task_reservation_timeout") Integer taskReservationTimeout
) {}

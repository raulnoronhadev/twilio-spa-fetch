package twilio_spa_fetch_backend.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.Map;

public record WorkflowRecordDTO(
        @JsonProperty("account_sid") String accountSid,
        @JsonProperty("assignment_callback_url") String assignmentCallbackUrl,
        String configuration,
        @JsonProperty("date_created") Date dateCreated,
        @JsonProperty("date_updated") Date dateUpdated,
        @JsonProperty("document_content_type") String documentContentType,
        @JsonProperty("fallback_assignment_callback_url") String fallbackAssignmentCallbackUrl,
        @JsonProperty("friendly_name") String friendlyName,
        String sid,
        @JsonProperty("task_reservation_timeout") Integer taskReservationTimeout,
        String url,
        @JsonProperty("workspace_sid") String workspaceSid,
        Map<String, String> links
) {}

package twilio_spa_fetch_backend.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.Map;

public record TaskQueueRecordDTO(
        @JsonProperty("account_sid") String accountSid,
        @JsonProperty("assignment_activity_name") String assignmentActivityName,
        @JsonProperty("assignment_activity_sid") String assignmentActivitySid,
        @JsonProperty("date_created") Date dateCreated,
        @JsonProperty("date_updated") Date dateUpdated,
        @JsonProperty("friendly_name") String friendlyName,
        @JsonProperty("max_reserved_workers") Integer maxReservedWorkers,
        Map<String, String> links,
        @JsonProperty("reservation_activity_name") String reservationActivityName,
        @JsonProperty("reservation_activity_sid") String reservationActivitySid,
        String sid,
        @JsonProperty("target_workers") String targetWorkers,
        @JsonProperty("task_order") String taskOrder,
        String url,
        @JsonProperty("workspace_sid") String workspaceSid
) {}

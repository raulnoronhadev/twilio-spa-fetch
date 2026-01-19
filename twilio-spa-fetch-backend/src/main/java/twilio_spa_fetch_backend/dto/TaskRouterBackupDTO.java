package twilio_spa_fetch_backend.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.List;

public record TaskRouterBackupDTO(
        WorkspaceRecordDTO workspace,
        List<WorkerRecordDTO> workers,
        List<WorkflowRecordDTO> workflows,
        List<TaskQueueRecordDTO> queues,
        @JsonProperty("task_channel") List<TaskChannelRecordDTO> taskChannel,
        List<ActivityRecordDTO> activies
) {
}

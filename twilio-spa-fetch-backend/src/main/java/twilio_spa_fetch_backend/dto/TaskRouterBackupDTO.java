package twilio_spa_fetch_backend.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TaskRouterBackupDTO(
        WorkspaceResponse workspace,
        List<WorkerResponse> workers,
        List<WorkflowResponse> workflows,
        List<TaskQueueResponse> queues,
        @JsonProperty("task_channel") List<TaskChannelResponse> taskChannel,
        List<ActivityResponse> activies
) {
}

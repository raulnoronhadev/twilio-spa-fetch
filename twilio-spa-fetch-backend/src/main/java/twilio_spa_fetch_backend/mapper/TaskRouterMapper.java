package twilio_spa_fetch_backend.mapper;
import com.twilio.rest.taskrouter.v1.workspace.*;
import org.mapstruct.Mapper;
import twilio_spa_fetch_backend.dto.*;
import com.twilio.rest.taskrouter.v1.Workspace;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskRouterMapper {
    WorkspaceResponse workspaceToWorkspaceDTO(Workspace workspace);
    WorkerResponse workerToWorkerDTO(Worker worker);
    List<WorkerResponse> workerToWorkerDTOList(List<Worker> worker);
    WorkflowResponse workflowToWorkflowDTO(Workflow workflow);
    List<WorkflowResponse> workflowToWorkflowDTOList(List<Workflow> workflow);
    TaskQueueResponse taskQueueToTaskQueueDTO(TaskQueue taskQueue);
    List<TaskQueueResponse> taskQueueToTaskQueueDTOList(List<TaskQueue> taskQueue);
    TaskChannelResponse taskChannelToTaskChannelDTOList(TaskChannel taskChannel);
    List<TaskChannelResponse> taskChannelToTaskChannelDTOList(List<TaskChannel> taskChannel);
    ActivityResponse activityToActivityDTO(Activity activity);
    List<ActivityResponse> activityToActivityDTOList(List<Activity> activity);
}

package twilio_spa_fetch_backend.mapper;
import com.twilio.rest.taskrouter.v1.workspace.*;
import org.mapstruct.Mapper;
import twilio_spa_fetch_backend.dto.*;
import com.twilio.rest.taskrouter.v1.Workspace;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskRouterMapper {
    WorkspaceDTO workspaceToWorkspaceDTO(Workspace workspace);
    WorkerDTO workerToWorkerDTO(Worker worker);
    List<WorkerDTO> workerToWorkerDTOList(List<Worker> worker);
    WorkflowDTO workflowToWorkflowDTO(Workflow workflow);
    List<WorkflowDTO> workflowToWorkflowDTOList(List<Workflow> workflow);
    TaskQueueDTO taskQueueToTaskQueueDTO(TaskQueue taskQueue);
    List<TaskQueueDTO> taskQueueToTaskQueueDTOList(List<TaskQueue> taskQueue);
    TaskChannelDTO taskChannelToTaskChannelDTOList(TaskChannel taskChannel);
    List<TaskChannelDTO> taskChannelToTaskChannelDTOList(List<TaskChannel> taskChannel);
    ActivityDTO activityToActivityDTO(Activity activity);
    List<ActivityDTO> activityToActivityDTOList(List<Activity> activity);
}

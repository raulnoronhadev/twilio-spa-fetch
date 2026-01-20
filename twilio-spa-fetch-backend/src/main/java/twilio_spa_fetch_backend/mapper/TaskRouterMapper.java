package twilio_spa_fetch_backend.mapper;
import com.twilio.rest.taskrouter.v1.workspace.*;
import org.mapstruct.Mapper;
import twilio_spa_fetch_backend.dto.*;
import com.twilio.rest.taskrouter.v1.Workspace;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskRouterMapper {

    WorkspaceRecordDTO workspaceToWorkspaceRecordDTO(Workspace workspace);
    WorkerRecordDTO workerToWorkerRecordDTO(Worker worker);
    List<WorkerRecordDTO> workerToWorkerRecordDTOList(List<Worker> worker);
    WorkflowRecordDTO workflowToWorkflowRecordDTO(Workflow workflow);
    List<WorkflowRecordDTO> workflowToWorkflowRecordDTOList(List<Workflow> workflow);
    TaskQueueRecordDTO taskQueueToTaskQueueRecordDTO(TaskQueue taskQueue);
    List<TaskQueueRecordDTO> taskQueueToTaskQueueDTOList(List<TaskQueue> taskQueue);
    TaskChannelRecordDTO taskChannelToTaskChannelRecordDTOList(TaskChannel taskChannel);
    List<TaskChannelRecordDTO> taskChannelToTaskChannelRecordDTOList(List<TaskChannel> taskChannel);
    ActivityRecordDTO activityToActivityRecordDTO(Activity activity);
    List<ActivityRecordDTO> activityToActivityRecordDTOList(List<Activity> activity);
}

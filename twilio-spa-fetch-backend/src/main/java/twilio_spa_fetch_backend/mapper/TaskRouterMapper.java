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
    List<WorkflowRecordDTO> workflowToWorkflowRecordDTOList(List<Workflow> workflow);
    List<TaskQueueRecordDTO> taskQueueToTaskQueueDTOList(List<TaskQueue> taskQueue);
    List<TaskChannelRecordDTO> taskChannelToTaskChannelRecordDTO(List<TaskChannel> taskChannel);
    List<ActivityRecordDTO> activityToActivityRecordDTO(List<Activity> activity);
}

package twilio_spa_fetch_backend.service;
import com.twilio.base.ResourceSet;
import com.twilio.rest.taskrouter.v1.Workspace;
import com.twilio.rest.taskrouter.v1.workspace.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twilio_spa_fetch_backend.dto.*;
import twilio_spa_fetch_backend.mapper.TaskRouterMapper;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class TaskRouterService {

    @Autowired
    private TaskRouterMapper taskRouterMapper;
    private static final int DEFAULT_LIMIT = 50;

//    ResponseEntity<TaskRouterBackupDTO> taskRouterBackup(String workspaceSid) {
//        ResponseEntity<WorkspaceRecordDTO> workspace = getWorkspaceBySid(workspaceSid);
//        TaskRouterBackupDTO dto = new TaskRouterBackupDTO(
//                workspace,
//
//                )
//    }

    public WorkspaceDTO getWorkspaceBySid(String workspaceSid) {
        Workspace workspace = Workspace.fetcher(workspaceSid).fetch();
        return taskRouterMapper.workspaceToWorkspaceDTO(workspace);
    }

    public WorkerDTO getWorkerBySid(String workspaceSid, String workerSid) {
        Worker worker = Worker.fetcher(workspaceSid, workerSid).fetch();
        return taskRouterMapper.workerToWorkerDTO(worker);
    }

    public List<WorkerDTO> getAllWorkers(String workspaceSid) {
        ResourceSet<Worker> worker = Worker.reader(workspaceSid).limit(DEFAULT_LIMIT).read();
        List<Worker> workerList = StreamSupport.stream(worker.spliterator(), false).toList();
        return taskRouterMapper.workerToWorkerDTOList(workerList);
    }

    public WorkflowDTO getWorkflowBySid(String workspaceSid, String workflowSid) {
        Workflow workflow = Workflow.fetcher(workspaceSid, workflowSid).fetch();
        return taskRouterMapper.workflowToWorkflowDTO(workflow);
    }

    public List<WorkflowDTO> getAllWorkflows(String workspaceSid) {
        ResourceSet<Workflow> workflow = Workflow.reader(workspaceSid).limit(DEFAULT_LIMIT).read();
        List<Workflow> workflowList = StreamSupport.stream(workflow.spliterator(), false).toList();
        return taskRouterMapper.workflowToWorkflowDTOList(workflowList);
    }

    public TaskQueueDTO getTaskQueueBySid(String workspaceSid, String taskQueueSid) {
        TaskQueue taskQueue = TaskQueue.fetcher(workspaceSid, taskQueueSid).fetch();
        return taskRouterMapper.taskQueueToTaskQueueDTO(taskQueue);
    }

    public List<TaskQueueDTO> getAllQueues(String workspaceSid) {
        ResourceSet<TaskQueue> taskQueue = TaskQueue.reader(workspaceSid).limit(DEFAULT_LIMIT).read();
        List<TaskQueue> taskQueueList = StreamSupport.stream(taskQueue.spliterator(), false).toList();
        return taskRouterMapper.taskQueueToTaskQueueDTOList(taskQueueList);
    }

    public TaskChannelDTO getTaskChannelBySid(String workspaceSid, String taskChannelSid) {
        TaskChannel taskChannel = TaskChannel.fetcher(workspaceSid, taskChannelSid).fetch();
        return taskRouterMapper.taskChannelToTaskChannelDTOList(taskChannel);
    }

    public List<TaskChannelDTO> getAllChannels(String workspaceSid) {
        ResourceSet<TaskChannel> taskChannel = TaskChannel.reader(workspaceSid).limit(DEFAULT_LIMIT).read();
        List<TaskChannel> taskChannelList = StreamSupport.stream(taskChannel.spliterator(), false).toList();
        return taskRouterMapper.taskChannelToTaskChannelDTOList(taskChannelList);
    }

    public ActivityDTO getActivityBySid(String workspaceSid, String activitySid) {
        Activity activity = Activity.fetcher(workspaceSid, activitySid).fetch();
        return taskRouterMapper.activityToActivityDTO(activity);
    }

    public List<ActivityDTO> getAllActivities(String workspaceSid) {
        ResourceSet<Activity> activity = Activity.reader(workspaceSid).limit(DEFAULT_LIMIT).read();
        List<Activity> activitiesList = StreamSupport.stream(activity.spliterator(), false).toList();
        return taskRouterMapper.activityToActivityDTOList(activitiesList);
    }
}
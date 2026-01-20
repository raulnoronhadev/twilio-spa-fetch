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

    public WorkspaceRecordDTO getWorkspaceBySid(String workspaceSid) {
        Workspace workspace = Workspace.fetcher(workspaceSid).fetch();
        return taskRouterMapper.workspaceToWorkspaceRecordDTO(workspace);
    }

    public WorkerRecordDTO getWorkerBySid(String workspaceSid, String workerSid) {
        Worker worker = Worker.fetcher(workspaceSid, workerSid).fetch();
        return taskRouterMapper.workerToWorkerRecordDTO(worker);
    }

    public List<WorkerRecordDTO> getAllWorkers(String workspaceSid) {
        ResourceSet<Worker> worker = Worker.reader(workspaceSid).limit(DEFAULT_LIMIT).read();
        List<Worker> workerList = StreamSupport.stream(worker.spliterator(), false).toList();
        return taskRouterMapper.workerToWorkerRecordDTOList(workerList);
    }

    public WorkflowRecordDTO getWorkflowBySid(String workspaceSid, String workflowSid) {
        Workflow workflow = Workflow.fetcher(workspaceSid, workflowSid).fetch();
        return taskRouterMapper.workflowToWorkflowRecordDTO(workflow);
    }

    public List<WorkflowRecordDTO> getAllWorkflows(String workspaceSid) {
        ResourceSet<Workflow> workflow = Workflow.reader(workspaceSid).limit(DEFAULT_LIMIT).read();
        List<Workflow> workflowList = StreamSupport.stream(workflow.spliterator(), false).toList();
        return taskRouterMapper.workflowToWorkflowRecordDTOList(workflowList);
    }

    public TaskQueueRecordDTO getTaskQueueBySid(String workspaceSid, String taskQueueSid) {
        TaskQueue taskQueue = TaskQueue.fetcher(workspaceSid, taskQueueSid).fetch();
        return taskRouterMapper.taskQueueToTaskQueueRecordDTO(taskQueue);
    }

    public List<TaskQueueRecordDTO> getAllQueues(String workspaceSid) {
        ResourceSet<TaskQueue> taskQueue = TaskQueue.reader(workspaceSid).limit(DEFAULT_LIMIT).read();
        List<TaskQueue> taskQueueList = StreamSupport.stream(taskQueue.spliterator(), false).toList();
        return taskRouterMapper.taskQueueToTaskQueueDTOList(taskQueueList);
    }

    public TaskChannelRecordDTO getTaskChannelBySid(String workspaceSid, String taskChannelSid) {
        TaskChannel taskChannel = TaskChannel.fetcher(workspaceSid, taskChannelSid).fetch();
        return taskRouterMapper.taskChannelToTaskChannelRecordDTOList(taskChannel);
    }

    public List<TaskChannelRecordDTO> getAllChannels(String workspaceSid) {
        ResourceSet<TaskChannel> taskChannel = TaskChannel.reader(workspaceSid).limit(DEFAULT_LIMIT).read();
        List<TaskChannel> taskChannelList = StreamSupport.stream(taskChannel.spliterator(), false).toList();
        return taskRouterMapper.taskChannelToTaskChannelRecordDTOList(taskChannelList);
    }

    public ActivityRecordDTO getActivityBySid(String workspaceSid, String activitySid) {
        Activity activity = Activity.fetcher(workspaceSid, activitySid).fetch();
        return taskRouterMapper.activityToActivityRecordDTO(activity);
    }

    public List<ActivityRecordDTO> getAllActivities(String workspaceSid) {
        ResourceSet<Activity> activity = Activity.reader(workspaceSid).limit(DEFAULT_LIMIT).read();
        List<Activity> activitiesList = StreamSupport.stream(activity.spliterator(), false).toList();
        return taskRouterMapper.activityToActivityRecordDTOList(activitiesList);
    }
}
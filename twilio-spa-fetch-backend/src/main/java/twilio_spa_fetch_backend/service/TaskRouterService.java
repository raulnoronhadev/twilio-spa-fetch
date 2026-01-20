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

    public List<WorkflowRecordDTO> getAllWorkflows(String workspaceSid) {
        ResourceSet<Workflow> workflow = Workflow.reader(workspaceSid).limit(DEFAULT_LIMIT).read();
        List<Workflow> workflowList = StreamSupport.stream(workflow.spliterator(), false).toList();
        return taskRouterMapper.workflowToWorkflowRecordDTOList(workflowList);
    }

    public List<TaskQueueRecordDTO> getAllQueues(String workspaceSid) {
        ResourceSet<TaskQueue> taskQueue = TaskQueue.reader(workspaceSid).limit(DEFAULT_LIMIT).read();
        List<TaskQueue> taskQueueList = StreamSupport.stream(taskQueue.spliterator(), false).toList();
        return taskRouterMapper.taskQueueToTaskQueueDTOList(taskQueueList);
    }

    public List<TaskChannelRecordDTO> getAllChannels(String workspaceSid) {
        ResourceSet<TaskChannel> taskChannel = TaskChannel.reader(workspaceSid).limit(DEFAULT_LIMIT).read();
        List<TaskChannel> taskChannelList = StreamSupport.stream(taskChannel.spliterator(), false).toList();
        return taskRouterMapper.taskChannelToTaskChannelRecordDTO(taskChannelList);
    }

    public List<ActivityRecordDTO> getAllActivities(String workspaceSid) {
        ResourceSet<Activity> activity = Activity.reader(workspaceSid).limit(DEFAULT_LIMIT).read();
        List<Activity> activitiesList = StreamSupport.stream(activity.spliterator(), false).toList();
        return taskRouterMapper.activityToActivityRecordDTO(activitiesList);
    }
}
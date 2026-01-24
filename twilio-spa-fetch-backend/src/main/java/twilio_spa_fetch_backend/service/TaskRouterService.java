package twilio_spa_fetch_backend.service;
import com.twilio.base.ResourceSet;
import com.twilio.rest.taskrouter.v1.Workspace;
import com.twilio.rest.taskrouter.v1.workspace.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twilio_spa_fetch_backend.dto.*;
import twilio_spa_fetch_backend.mapper.TaskRouterMapper;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import twilio_spa_fetch_backend.ports.StoragePort;

@Service
public class TaskRouterService {

    @Autowired
    private TaskRouterMapper taskRouterMapper;

    @Autowired
    private StoragePort storagePort;

    @Autowired
    private ObjectMapper objectMapper;

    private static final int DEFAULT_LIMIT = 50;

    public WorkspaceDTO getWorkspaceBySid(String workspaceSid) {
        Workspace workspace = Workspace.fetcher(workspaceSid).fetch();
        return taskRouterMapper.workspaceToWorkspaceDTO(workspace);
    }

    public WorkspaceDTO getFullWorkspace(String workspaceSid) {
        Workspace workspace = Workspace.fetcher(workspaceSid).fetch();
        List<WorkflowDTO> workflows = getAllWorkflows(workspaceSid);
        List<WorkerDTO> workers = getAllWorkers(workspaceSid);
        List<TaskChannelDTO> channels = getAllChannels(workspaceSid);
        List<TaskQueueDTO> queues = getAllQueues(workspaceSid);
        List<ActivityDTO> activities = getAllActivities(workspaceSid);
        return taskRouterMapper.workspaceToWorkspaceDTO(
                workspace, workflows, workers, channels, queues, activities
        );
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

    public String backupWorkspace(String workspaceSid) {
        WorkspaceDTO workspace = getFullWorkspace(workspaceSid);
        try {
            String jsonContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(workspace);
            byte[] jsonBytes = jsonContent.getBytes("UTF-8");
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String fileName = String.format("workspace/%s_%s.json", workspace.sid(), timestamp);
            return storagePort.uploadFile(jsonBytes, fileName, "application/json");
        } catch (Exception e) {
            throw new RuntimeException("Backup error: " + e.getMessage(), e);
        }
    }

    public String restoreWorkspace(String fileName) {
        try {
            byte[] jsonBytes = storagePort.downloadFile(fileName);
            String jsonContent = new String(jsonBytes, "UTF-8");
            WorkspaceDTO workspaceDTO = objectMapper.readValue(jsonContent, WorkspaceDTO.class);
            URI eventCallbackUrl = workspaceDTO.eventCallbackUrl();
            String friendlyName = workspaceDTO.friendlyName();
            Workspace workspace = Workspace.creator(friendlyName).setEventCallbackUrl(eventCallbackUrl).setTemplate("NONE").create();
            String newWorkspaceSid = workspace.getSid();
            System.out.println("Created new Workspace: " + newWorkspaceSid);
            Map<String, String> sidMapping = new HashMap<>();
            for (ActivityDTO act : workspaceDTO.activities()) {
                try {
                    Activity newActivity = Activity.creator(newWorkspaceSid, act.friendlyName()).create();
                    sidMapping.put(act.sid(), newActivity.getSid());
                } catch (Exception e) {
                    System.out.println("Activity " + act.friendlyName() + " already exists, skipping...");
                }
            }
            System.out.println("Restored activities");
            for (TaskChannelDTO channel : workspaceDTO.taskChannels()) {
                try {
                    TaskChannel.creator(newWorkspaceSid, channel.friendlyName(), channel.uniqueName()).create();
                } catch (Exception e) {
                    System.out.println("Channel " + channel.uniqueName() + " already exists, skipping...");
                }
            }
            System.out.println("Restored channels");
            for (TaskQueueDTO queue : workspaceDTO.taskQueues()) {
                TaskQueue newQueue = TaskQueue.creator(newWorkspaceSid, queue.friendlyName()).create();
                sidMapping.put(queue.sid(), newQueue.getSid());
            }
            System.out.println("Restored queues and mapped SIDs");
            for (WorkerDTO worker : workspaceDTO.workers()) {
                Worker.creator(newWorkspaceSid, worker.friendlyName()).setAttributes(worker.attributes()).create();
            }
            System.out.println("Restored workers");
            for (WorkflowDTO workflow : workspaceDTO.workflows()) {
                String config = workflow.configuration();
                for (Map.Entry<String, String> entry : sidMapping.entrySet()) {
                    config = config.replace(entry.getKey(), entry.getValue());
                }
                Workflow.creator(newWorkspaceSid, workflow.friendlyName(), config).setAssignmentCallbackUrl(workflow.assignmentCallbackUrl()).create();
            }
            System.out.println("Restored workflows");
            return newWorkspaceSid;
        } catch (Exception e) {
            throw new RuntimeException("Error restoring Workspace " + e.getMessage(), e);
        }
    }
}
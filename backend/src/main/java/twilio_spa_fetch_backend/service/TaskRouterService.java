package twilio_spa_fetch_backend.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.twilio.base.Page;
import com.twilio.base.Reader;
import com.twilio.base.Resource;
import com.twilio.base.ResourceSet;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.Domains;
import com.twilio.rest.taskrouter.v1.Workspace;
import com.twilio.rest.taskrouter.v1.workspace.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twilio_spa_fetch_backend.dto.*;
import twilio_spa_fetch_backend.mapper.TaskRouterMapper;
import twilio_spa_fetch_backend.security.TwilioClientProvider;
import twilio_spa_fetch_backend.util.PageTokenCodec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    private static final Logger log = LoggerFactory.getLogger(TaskRouterService.class);

    @Autowired
    private TaskRouterMapper taskRouterMapper;

    @Autowired
    private StoragePort storagePort;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TwilioClientProvider twilioClientProvider;

    private static final int DEFAULT_LIMIT = 50;

    public WorkspaceDTO getWorkspaceBySid(String workspaceSid) {
        Workspace workspace = Workspace.fetcher(workspaceSid).fetch(twilioClientProvider.getClient());
        return taskRouterMapper.workspaceToWorkspaceDTO(workspace);
    }

    public WorkspaceDTO getFullWorkspace(String workspaceSid) {
        Workspace workspace = Workspace.fetcher(workspaceSid).fetch(twilioClientProvider.getClient());
        List<WorkflowDTO> workflows = getAllWorkflows(workspaceSid);
        List<WorkerDTO> workers = getAllWorkers(workspaceSid);
        List<TaskChannelDTO> channels = getAllChannels(workspaceSid);
        List<TaskQueueDTO> queues = getAllQueues(workspaceSid);
        List<ActivityDTO> activities = getAllActivities(workspaceSid);
        return taskRouterMapper.workspaceToWorkspaceDTO(
                workspace, workflows, workers, channels, queues, activities
        );
    }

    /** Fetches one page of any TaskRouter child resource and maps its records. */
    private <T extends Resource, D> PageDTO<D> readPage(Reader<T> reader, int pageSize, String pageToken,
                                                        java.util.function.Function<List<T>, List<D>> mapper) {
        TwilioRestClient client = twilioClientProvider.getClient();
        reader.pageSize(Math.clamp(pageSize, 1, 100));
        Page<T> page = pageToken == null
                ? reader.firstPage(client)
                : reader.getPage(PageTokenCodec.decode(pageToken), client);
        String nextToken = page.hasNextPage()
                ? PageTokenCodec.encode(page.getNextPageUrl(Domains.TASKROUTER.toString()))
                : null;
        return new PageDTO<>(mapper.apply(page.getRecords()), nextToken);
    }

    public PageDTO<WorkerDTO> getWorkers(String workspaceSid, int pageSize, String pageToken) {
        return readPage(Worker.reader(workspaceSid), pageSize, pageToken, taskRouterMapper::workerToWorkerDTOList);
    }

    public PageDTO<WorkflowDTO> getWorkflows(String workspaceSid, int pageSize, String pageToken) {
        return readPage(Workflow.reader(workspaceSid), pageSize, pageToken, taskRouterMapper::workflowToWorkflowDTOList);
    }

    public PageDTO<TaskQueueDTO> getTaskQueues(String workspaceSid, int pageSize, String pageToken) {
        return readPage(TaskQueue.reader(workspaceSid), pageSize, pageToken, taskRouterMapper::taskQueueToTaskQueueDTOList);
    }

    public PageDTO<TaskChannelDTO> getTaskChannels(String workspaceSid, int pageSize, String pageToken) {
        return readPage(TaskChannel.reader(workspaceSid), pageSize, pageToken, taskRouterMapper::taskChannelToTaskChannelDTOList);
    }

    public PageDTO<ActivityDTO> getActivities(String workspaceSid, int pageSize, String pageToken) {
        return readPage(Activity.reader(workspaceSid), pageSize, pageToken, taskRouterMapper::activityToActivityDTOList);
    }

    public WorkerDTO getWorkerBySid(String workspaceSid, String workerSid) {
        Worker worker = Worker.fetcher(workspaceSid, workerSid).fetch(twilioClientProvider.getClient());
        return taskRouterMapper.workerToWorkerDTO(worker);
    }

    public List<WorkerDTO> getAllWorkers(String workspaceSid) {
        ResourceSet<Worker> worker = Worker.reader(workspaceSid).limit(DEFAULT_LIMIT).read(twilioClientProvider.getClient());
        List<Worker> workerList = StreamSupport.stream(worker.spliterator(), false).toList();
        return taskRouterMapper.workerToWorkerDTOList(workerList);
    }

    public WorkflowDTO getWorkflowBySid(String workspaceSid, String workflowSid) {
        Workflow workflow = Workflow.fetcher(workspaceSid, workflowSid).fetch(twilioClientProvider.getClient());
        return taskRouterMapper.workflowToWorkflowDTO(workflow);
    }

    public List<WorkflowDTO> getAllWorkflows(String workspaceSid) {
        ResourceSet<Workflow> workflow = Workflow.reader(workspaceSid).limit(DEFAULT_LIMIT).read(twilioClientProvider.getClient());
        List<Workflow> workflowList = StreamSupport.stream(workflow.spliterator(), false).toList();
        return taskRouterMapper.workflowToWorkflowDTOList(workflowList);
    }

    public TaskQueueDTO getTaskQueueBySid(String workspaceSid, String taskQueueSid) {
        TaskQueue taskQueue = TaskQueue.fetcher(workspaceSid, taskQueueSid).fetch(twilioClientProvider.getClient());
        return taskRouterMapper.taskQueueToTaskQueueDTO(taskQueue);
    }

    public List<TaskQueueDTO> getAllQueues(String workspaceSid) {
        ResourceSet<TaskQueue> taskQueue = TaskQueue.reader(workspaceSid).limit(DEFAULT_LIMIT).read(twilioClientProvider.getClient());
        List<TaskQueue> taskQueueList = StreamSupport.stream(taskQueue.spliterator(), false).toList();
        return taskRouterMapper.taskQueueToTaskQueueDTOList(taskQueueList);
    }

    public TaskChannelDTO getTaskChannelBySid(String workspaceSid, String taskChannelSid) {
        TaskChannel taskChannel = TaskChannel.fetcher(workspaceSid, taskChannelSid).fetch(twilioClientProvider.getClient());
        return taskRouterMapper.taskChannelToTaskChannelDTOList(taskChannel);
    }

    public List<TaskChannelDTO> getAllChannels(String workspaceSid) {
        ResourceSet<TaskChannel> taskChannel = TaskChannel.reader(workspaceSid).limit(DEFAULT_LIMIT).read(twilioClientProvider.getClient());
        List<TaskChannel> taskChannelList = StreamSupport.stream(taskChannel.spliterator(), false).toList();
        return taskRouterMapper.taskChannelToTaskChannelDTOList(taskChannelList);
    }

    public ActivityDTO getActivityBySid(String workspaceSid, String activitySid) {
        Activity activity = Activity.fetcher(workspaceSid, activitySid).fetch(twilioClientProvider.getClient());
        return taskRouterMapper.activityToActivityDTO(activity);
    }

    public List<ActivityDTO> getAllActivities(String workspaceSid) {
        ResourceSet<Activity> activity = Activity.reader(workspaceSid).limit(DEFAULT_LIMIT).read(twilioClientProvider.getClient());
        List<Activity> activitiesList = StreamSupport.stream(activity.spliterator(), false).toList();
        return taskRouterMapper.activityToActivityDTOList(activitiesList);
    }

    public String backupWorkspace(String workspaceSid) {
        WorkspaceDTO workspace = getFullWorkspace(workspaceSid);
        byte[] jsonBytes = serializeWorkspace(workspace);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String fileName = String.format("workspace/%s_%s.json", workspace.sid(), timestamp);
        return storagePort.uploadFile(jsonBytes, fileName, "application/json");
    }

    public String restoreWorkspace(String fileName) {
        TwilioRestClient client = twilioClientProvider.getClient();
        WorkspaceDTO workspaceDTO = loadWorkspaceFromFile(fileName);
        String newWorkspaceSid = createWorkspace(workspaceDTO, client);
        Map<String, String> sidMapping = new HashMap<>();
        restoreActivities(newWorkspaceSid, workspaceDTO.activities(), sidMapping, client);
        restoreTaskChannels(newWorkspaceSid, workspaceDTO.taskChannels(), client);
        restoreTaskQueues(newWorkspaceSid, workspaceDTO.taskQueues(), sidMapping, client);
        restoreWorkers(newWorkspaceSid, workspaceDTO.workers(), client);
        restoreWorkflows(newWorkspaceSid, workspaceDTO.workflows(), sidMapping, client);
        log.info("Workspace restoration completed: {}", newWorkspaceSid);
        return newWorkspaceSid;
    }

    private byte[] serializeWorkspace(WorkspaceDTO workspace) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(workspace)
                    .getBytes(StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize workspace " + workspace.sid(), e);
        }
    }

    private WorkspaceDTO loadWorkspaceFromFile(String fileName) {
        byte[] jsonBytes = storagePort.downloadFile(fileName);
        try {
            return objectMapper.readValue(jsonBytes, WorkspaceDTO.class);
        } catch (IOException e) {
            throw new IllegalStateException("Backup file is not a valid workspace backup: " + fileName, e);
        }
    }

    private String createWorkspace(WorkspaceDTO workspaceDTO, TwilioRestClient client) {
        Workspace workspace = Workspace.creator(workspaceDTO.friendlyName())
                .setEventCallbackUrl(workspaceDTO.eventCallbackUrl())
                .setTemplate("NONE")
                .create(client);
        String sid = workspace.getSid();
        log.info("Created workspace {}", sid);
        return sid;
    }

    private void restoreActivities(String workspaceSid, List<ActivityDTO> activities, Map<String, String> sidMapping, TwilioRestClient client) {
        for (ActivityDTO activity : activities) {
            try {
                Activity newActivity = Activity.creator(workspaceSid, activity.friendlyName()).create(client);
                sidMapping.put(activity.sid(), newActivity.getSid());
            } catch (Exception e) {
                log.info("Activity '{}' already exists, skipping...", activity.friendlyName());
            }
        }
        log.info("Restored {} activities", activities.size());
    }

    private void restoreTaskChannels(String workspaceSid, List<TaskChannelDTO> channels, TwilioRestClient client) {
        for (TaskChannelDTO channel : channels) {
            try {
                TaskChannel.creator(workspaceSid, channel.friendlyName(), channel.uniqueName()).create(client);
            } catch (Exception e) {
                log.info("Channel '{}' already exists, skipping...", channel.uniqueName());
            }
        }
        log.info("Restored {} task channels", channels.size());
    }

    private void restoreTaskQueues(String workspaceSid, List<TaskQueueDTO> queues, Map<String, String> sidMapping, TwilioRestClient client) {
        for (TaskQueueDTO queue : queues) {
            TaskQueue newQueue = TaskQueue.creator(workspaceSid, queue.friendlyName()).create(client);
            sidMapping.put(queue.sid(), newQueue.getSid());
        }
        log.info("Restored {} task queues", queues.size());
    }

    private void restoreWorkers(String workspaceSid, List<WorkerDTO> workers, TwilioRestClient client) {
        for (WorkerDTO worker : workers) {
            Worker.creator(workspaceSid, worker.friendlyName()).setAttributes(worker.attributes()).create(client);
        }
        log.info("Restored {} workers", workers.size());
    }

    private void restoreWorkflows(String workspaceSid, List<WorkflowDTO> workflows, Map<String, String> sidMapping, TwilioRestClient client) {
        for (WorkflowDTO workflow : workflows) {
            String updatedConfig = replaceSidsInConfiguration(workflow.configuration(), sidMapping);

            Workflow.creator(workspaceSid, workflow.friendlyName(), updatedConfig)
                    .setAssignmentCallbackUrl(workflow.assignmentCallbackUrl())
                    .create(client);
        }
        log.info("Restored {} workflows", workflows.size());
    }

    private String replaceSidsInConfiguration(String configuration, Map<String, String> sidMapping) {
        String result = configuration;
        for (Map.Entry<String, String> entry : sidMapping.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        return result;
    }
}

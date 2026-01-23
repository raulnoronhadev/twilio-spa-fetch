package twilio_spa_fetch_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import twilio_spa_fetch_backend.dto.*;
import twilio_spa_fetch_backend.service.TaskRouterService;

import java.util.List;

@RestController
@RequestMapping("/TaskRouter")
public class TaskRouterController {

    @Autowired
    TaskRouterService taskRouterService;

    @GetMapping("/{workspaceSid}")
    public ResponseEntity<WorkspaceDTO> getWorkspaceBySid(@PathVariable String workspaceSid) {
        return ResponseEntity.ok(taskRouterService.getWorkspaceBySid(workspaceSid));
    }

    @GetMapping("/FullWorkspace/{workspaceSid}")
    public ResponseEntity<WorkspaceDTO> getFullWorkspace(@PathVariable String workspaceSid) {
        return ResponseEntity.ok(taskRouterService.getFullWorkspace(workspaceSid));
    }

    @GetMapping("/{workspaceSid}/Workers/{workerSid}")
    public ResponseEntity<WorkerDTO> getWorkerBySid(@PathVariable String workspaceSid, @PathVariable String workerSid) {
        return ResponseEntity.ok(taskRouterService.getWorkerBySid(workspaceSid, workerSid));
    }

    @GetMapping("/{workspaceSid}/Workers")
    public ResponseEntity<List<WorkerDTO>> getAllWorkers(@PathVariable String workspaceSid) {
        return ResponseEntity.ok(taskRouterService.getAllWorkers(workspaceSid));
    }

    @GetMapping("/{workspaceSid}/Workflows/{workflowSid}")
    public ResponseEntity<WorkflowDTO> getWorkflowBySid(@PathVariable String workspaceSid, @PathVariable String workflowSid) {
        return ResponseEntity.ok(taskRouterService.getWorkflowBySid(workspaceSid, workflowSid));
    }

    @GetMapping("/{workspaceSid}/Workflows")
    public ResponseEntity<List<WorkflowDTO>> getAllWorkflows(@PathVariable String workspaceSid) {
        return ResponseEntity.ok(taskRouterService.getAllWorkflows(workspaceSid));
    }

    @GetMapping("/{workspaceSid}/TaskQueues/{taskQueueSid}")
    public ResponseEntity<TaskQueueDTO> getTaskQueueBySid(@PathVariable String workspaceSid, @PathVariable String taskQueueSid) {
        return ResponseEntity.ok(taskRouterService.getTaskQueueBySid(workspaceSid, taskQueueSid));
    }

    @GetMapping("/{workspaceSid}/TaskQueues")
    public ResponseEntity<List<TaskQueueDTO>> getAllQueues(@PathVariable String workspaceSid) {
        return ResponseEntity.ok(taskRouterService.getAllQueues(workspaceSid));
    }

    @GetMapping("/{workspaceSid}/TaskChannels/{taskChannelSid}")
    public ResponseEntity<TaskChannelDTO> getTaskChannelBySid(@PathVariable String workspaceSid, @PathVariable String taskChannelSid) {
        return ResponseEntity.ok(taskRouterService.getTaskChannelBySid(workspaceSid, taskChannelSid));
    }

    @GetMapping("/{workspaceSid}/TaskChannels")
    public ResponseEntity<List<TaskChannelDTO>> getAllChannels(@PathVariable String workspaceSid) {
        return ResponseEntity.ok(taskRouterService.getAllChannels(workspaceSid));
    }

    @GetMapping("/{workspaceSid}/Activities/{activitySid}")
    public ResponseEntity<ActivityDTO> getActivityBySid(@PathVariable String workspaceSid, @PathVariable String activitySid) {
        return ResponseEntity.ok(taskRouterService.getActivityBySid(workspaceSid, activitySid));
    }

    @GetMapping("/{workspaceSid}/Activities")
    public ResponseEntity<List<ActivityDTO>> getAllActivities(@PathVariable String workspaceSid) {
        return ResponseEntity.ok(taskRouterService.getAllActivities(workspaceSid));
    }

}

package twilio_spa_fetch_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import twilio_spa_fetch_backend.dto.*;
import twilio_spa_fetch_backend.service.TaskRouterService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/task-router/workspaces")
public class TaskRouterController {

    @Autowired
    TaskRouterService taskRouterService;

    @GetMapping("/{workspaceSid}")
    public ResponseEntity<WorkspaceDTO> getWorkspaceBySid(@PathVariable String workspaceSid) {
        return ResponseEntity.ok(taskRouterService.getWorkspaceBySid(workspaceSid));
    }

    @GetMapping("/{workspaceSid}/complete")
    public ResponseEntity<WorkspaceDTO> getCompleteWorkspaceConfiguration(@PathVariable String workspaceSid) {
        return ResponseEntity.ok(taskRouterService.getFullWorkspace(workspaceSid));
    }

    @GetMapping("/{workspaceSid}/workers")
    public ResponseEntity<PageDTO<WorkerDTO>> getWorkers(
            @PathVariable String workspaceSid,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String pageToken) {
        return ResponseEntity.ok(taskRouterService.getWorkers(workspaceSid, pageSize, pageToken));
    }

    @GetMapping("/{workspaceSid}/workers/{workerSid}")
    public ResponseEntity<WorkerDTO> getWorkerBySid(@PathVariable String workspaceSid, @PathVariable String workerSid) {
        return ResponseEntity.ok(taskRouterService.getWorkerBySid(workspaceSid, workerSid));
    }

    @GetMapping("/{workspaceSid}/workflows")
    public ResponseEntity<PageDTO<WorkflowDTO>> getWorkflows(
            @PathVariable String workspaceSid,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String pageToken) {
        return ResponseEntity.ok(taskRouterService.getWorkflows(workspaceSid, pageSize, pageToken));
    }

    @GetMapping("/{workspaceSid}/workflows/{workflowSid}")
    public ResponseEntity<WorkflowDTO> getWorkflowBySid(@PathVariable String workspaceSid, @PathVariable String workflowSid) {
        return ResponseEntity.ok(taskRouterService.getWorkflowBySid(workspaceSid, workflowSid));
    }

    @GetMapping("/{workspaceSid}/task-queues")
    public ResponseEntity<PageDTO<TaskQueueDTO>> getTaskQueues(
            @PathVariable String workspaceSid,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String pageToken) {
        return ResponseEntity.ok(taskRouterService.getTaskQueues(workspaceSid, pageSize, pageToken));
    }

    @GetMapping("/{workspaceSid}/task-queues/{taskQueueSid}")
    public ResponseEntity<TaskQueueDTO> getTaskQueueBySid(@PathVariable String workspaceSid, @PathVariable String taskQueueSid) {
        return ResponseEntity.ok(taskRouterService.getTaskQueueBySid(workspaceSid, taskQueueSid));
    }

    @GetMapping("/{workspaceSid}/task-channels")
    public ResponseEntity<PageDTO<TaskChannelDTO>> getTaskChannels(
            @PathVariable String workspaceSid,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String pageToken) {
        return ResponseEntity.ok(taskRouterService.getTaskChannels(workspaceSid, pageSize, pageToken));
    }

    @GetMapping("/{workspaceSid}/task-channels/{taskChannelSid}")
    public ResponseEntity<TaskChannelDTO> getTaskChannelBySid(@PathVariable String workspaceSid, @PathVariable String taskChannelSid) {
        return ResponseEntity.ok(taskRouterService.getTaskChannelBySid(workspaceSid, taskChannelSid));
    }

    @GetMapping("/{workspaceSid}/activities")
    public ResponseEntity<PageDTO<ActivityDTO>> getActivities(
            @PathVariable String workspaceSid,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String pageToken) {
        return ResponseEntity.ok(taskRouterService.getActivities(workspaceSid, pageSize, pageToken));
    }

    @GetMapping("/{workspaceSid}/activities/{activitySid}")
    public ResponseEntity<ActivityDTO> getActivityBySid(@PathVariable String workspaceSid, @PathVariable String activitySid) {
        return ResponseEntity.ok(taskRouterService.getActivityBySid(workspaceSid, activitySid));
    }

    @PostMapping("/{workspaceSid}/backup")
    public ResponseEntity<String> backupWorkspace(@PathVariable String workspaceSid) {
        return ResponseEntity.ok(taskRouterService.backupWorkspace(workspaceSid));
    }

    @PostMapping("/restore")
    public ResponseEntity<Map<String, String>> restoreWorkspace(@RequestBody Map<String, String> request) {
        String fileName = request.get("fileName");
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("fileName is required");
        }
        String newWorkspaceSid = taskRouterService.restoreWorkspace(fileName);
        return ResponseEntity.ok(Map.of(
                "message", "Workspace restored successfully", "newWorkspaceSid", newWorkspaceSid, "restoredFrom", fileName
        ));
    }

}

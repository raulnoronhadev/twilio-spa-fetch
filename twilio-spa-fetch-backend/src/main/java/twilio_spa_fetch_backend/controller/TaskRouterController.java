package twilio_spa_fetch_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import twilio_spa_fetch_backend.dto.WorkspaceRecordDTO;
import twilio_spa_fetch_backend.service.TaskRouterService;

@RestController
@RequestMapping("/TaskRouter")
public class TaskRouterController {

    @Autowired
    TaskRouterService taskRouterService;

    @GetMapping("/workspaces/{workspaceSid}")
    public ResponseEntity<WorkspaceRecordDTO> getWorkspace(@PathVariable String workspaceSid) {
        return ResponseEntity.ok(taskRouterService.getWorkspaceBySid(workspaceSid));
    }

}

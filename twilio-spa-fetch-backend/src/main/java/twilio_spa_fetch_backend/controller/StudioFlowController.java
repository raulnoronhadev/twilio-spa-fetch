package twilio_spa_fetch_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import twilio_spa_fetch_backend.dto.FlowDTO;
import twilio_spa_fetch_backend.service.StudioFlowService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Studio")
public class StudioFlowController {

    @Autowired
    StudioFlowService studioFlowService;

    @GetMapping("/Flows")
    public ResponseEntity<List<FlowDTO>> getAllFlows() {
        return ResponseEntity.ok(studioFlowService.getAllFlows());
    }

    @GetMapping("/Flows/{flowSid}")
    public ResponseEntity<FlowDTO> getFlowBySID(@PathVariable String flowSid) {
        return ResponseEntity.ok(studioFlowService.getFlowBySid(flowSid));
    }

    @GetMapping("/Flows/{flowSid}/definition")
    public ResponseEntity<Object> getFlowDefinition(@PathVariable String flowSid) {
        return ResponseEntity.ok(studioFlowService.getDefinitionBySid(flowSid));
    }

    @PostMapping("/Flows/{flowSid}/backup")
    public ResponseEntity<String> backupFlow(@PathVariable String flowSid) {
        String fileUrl = studioFlowService.backupFlowBySid(flowSid);
        return ResponseEntity.ok(fileUrl);
    }

    @PostMapping("/Flows/backup")
    public ResponseEntity<Object> backupAllFlows() {
        List<String> fileUrls = studioFlowService.backupAllFlows();
        return ResponseEntity.ok(fileUrls);
    }

    @PostMapping("/Flows/restore")
    public ResponseEntity<Map<String, String>> restoreFlow(@RequestBody Map<String, String> request) {
        String fileName = request.get("fileName");
        if (fileName == null || fileName.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "fileName is required"));
        }
        String newFlowSid = studioFlowService.restoreDeletedFlow(fileName);
        return ResponseEntity.ok(Map.of(
                "message", "Flow restored sucessfully", "newFlowSid", newFlowSid,  "restoredFrom", fileName
        ));
    }
}

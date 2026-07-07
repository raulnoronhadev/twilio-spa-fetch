package twilio_spa_fetch_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import twilio_spa_fetch_backend.dto.FlowDTO;
import twilio_spa_fetch_backend.dto.PageDTO;
import twilio_spa_fetch_backend.service.StudioFlowService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/studio/flows")
public class StudioFlowController {

    @Autowired
    StudioFlowService studioFlowService;

    @GetMapping
    public ResponseEntity<PageDTO<FlowDTO>> getFlows(
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String pageToken) {
        return ResponseEntity.ok(studioFlowService.getFlows(pageSize, pageToken));
    }

    @GetMapping("/{flowSid}")
    public ResponseEntity<FlowDTO> getFlowBySid(@PathVariable String flowSid) {
        return ResponseEntity.ok(studioFlowService.getFlowBySid(flowSid));
    }

    @GetMapping("/{flowSid}/definition")
    public ResponseEntity<Object> getFlowDefinition(@PathVariable String flowSid) {
        return ResponseEntity.ok(studioFlowService.getDefinitionBySid(flowSid));
    }

    @PostMapping("/{flowSid}/backup")
    public ResponseEntity<String> backupFlow(@PathVariable String flowSid) {
        String fileUrl = studioFlowService.backupFlowBySid(flowSid);
        return ResponseEntity.ok(fileUrl);
    }

    @PostMapping("/backup")
    public ResponseEntity<List<String>> backupAllFlows() {
        return ResponseEntity.ok(studioFlowService.backupAllFlows());
    }

    @PostMapping("/restore")
    public ResponseEntity<Map<String, String>> restoreFlow(@RequestBody Map<String, String> request) {
        String fileName = request.get("fileName");
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("fileName is required");
        }
        String newFlowSid = studioFlowService.restoreDeletedFlow(fileName);
        return ResponseEntity.ok(Map.of(
                "message", "Flow restored successfully", "newFlowSid", newFlowSid, "restoredFrom", fileName
        ));
    }
}

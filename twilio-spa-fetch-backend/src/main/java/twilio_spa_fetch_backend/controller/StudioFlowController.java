package twilio_spa_fetch_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import twilio_spa_fetch_backend.dto.FlowRecordDTO;
import twilio_spa_fetch_backend.service.StudioFlowService;

import java.util.List;

@RestController
@RequestMapping("/Studio")
public class StudioFlowController {

    @Autowired
    StudioFlowService studioFlowService;

    @GetMapping("/Flow")
    public ResponseEntity<List<FlowRecordDTO>> getAllFlows() {
        return studioFlowService.findAllFlows();
    }

    @GetMapping("/Flow/{flowSid}")
    public ResponseEntity<FlowRecordDTO> getFlowBySID(@PathVariable String flowSid) {
        return studioFlowService.getFlowBySid(flowSid);
    }

    @GetMapping("/Flow/{flowSid}/definition")
    public ResponseEntity<Object> getFlowDefinition(@PathVariable String flowSid) {
        return ResponseEntity.ok(studioFlowService.getDefinitionBySid(flowSid));
    }
}

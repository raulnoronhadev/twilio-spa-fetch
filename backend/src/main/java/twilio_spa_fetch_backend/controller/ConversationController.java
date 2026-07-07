package twilio_spa_fetch_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import twilio_spa_fetch_backend.dto.ConversationDTO;
import twilio_spa_fetch_backend.dto.PageDTO;
import twilio_spa_fetch_backend.service.ConversationService;

@RestController
@RequestMapping("/api/v1/conversations")
public class ConversationController {

    @Autowired
    ConversationService conversationService;

    @GetMapping
    public ResponseEntity<PageDTO<ConversationDTO>> getConversations(
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String pageToken) {
        return ResponseEntity.ok(conversationService.getConversations(pageSize, pageToken));
    }

}

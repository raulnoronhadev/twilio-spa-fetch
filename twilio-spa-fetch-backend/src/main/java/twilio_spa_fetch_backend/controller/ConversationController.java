package twilio_spa_fetch_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import twilio_spa_fetch_backend.dto.ConversationDTO;
import twilio_spa_fetch_backend.service.ConversationService;

import java.util.List;

@RestController
@RequestMapping("/Conversation")
public class ConversationController {

    @Autowired
    ConversationService conversationService;

    @RequestMapping("/List")
    public ResponseEntity<List<ConversationDTO>> getMultipleConversations() {
        return ResponseEntity.ok(conversationService.getMultipleConversation());
    }

}

package twilio_spa_fetch_backend.service;

import com.twilio.base.ResourceSet;
import com.twilio.rest.conversations.v1.Conversation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twilio_spa_fetch_backend.dto.ConversationDTO;
import twilio_spa_fetch_backend.mapper.ConversationMapper;
import twilio_spa_fetch_backend.security.TwilioClientProvider;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ConversationService {

    @Autowired
    ConversationMapper conversationMapper;

    @Autowired
    TwilioClientProvider twilioClientProvider;

    public List<ConversationDTO> getMultipleConversation() {
        ResourceSet<Conversation> conversation = Conversation.reader().read(twilioClientProvider.getClient());
        List<Conversation> conversationList = StreamSupport.stream(conversation.spliterator(), false).toList();
        return conversationMapper.conversationToConversationDTOList(conversationList);
    }

}

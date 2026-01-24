package twilio_spa_fetch_backend.mapper;
import com.twilio.rest.conversations.v1.Conversation;
import org.mapstruct.Mapper;
import twilio_spa_fetch_backend.dto.ConversationDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConversationMapper {

    List<ConversationDTO> conversationToConversationDTOList(List<Conversation> conversation);

}

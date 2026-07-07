package twilio_spa_fetch_backend.service;

import com.twilio.base.Page;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.Domains;
import com.twilio.rest.conversations.v1.Conversation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twilio_spa_fetch_backend.dto.ConversationDTO;
import twilio_spa_fetch_backend.dto.PageDTO;
import twilio_spa_fetch_backend.mapper.ConversationMapper;
import twilio_spa_fetch_backend.security.TwilioClientProvider;
import twilio_spa_fetch_backend.util.PageTokenCodec;
import java.util.List;

@Service
public class ConversationService {

    @Autowired
    ConversationMapper conversationMapper;

    @Autowired
    TwilioClientProvider twilioClientProvider;

    public PageDTO<ConversationDTO> getConversations(int pageSize, String pageToken) {
        TwilioRestClient client = twilioClientProvider.getClient();
        var reader = Conversation.reader().pageSize(Math.clamp(pageSize, 1, 100));
        Page<Conversation> page = pageToken == null
                ? reader.firstPage(client)
                : reader.getPage(PageTokenCodec.decode(pageToken), client);
        List<ConversationDTO> items = conversationMapper.conversationToConversationDTOList(page.getRecords());
        String nextToken = page.hasNextPage()
                ? PageTokenCodec.encode(page.getNextPageUrl(Domains.CONVERSATIONS.toString()))
                : null;
        return new PageDTO<>(items, nextToken);
    }

}

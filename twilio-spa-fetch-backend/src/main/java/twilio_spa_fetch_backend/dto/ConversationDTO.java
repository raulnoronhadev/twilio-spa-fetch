package twilio_spa_fetch_backend.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.twilio.rest.conversations.v1.Conversation;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Map;

public record ConversationDTO(
        @JsonProperty("account_sid") String accountSid,
        @JsonProperty("chat_service_sid") String chatServiceSid,
        @JsonProperty("messaging_service_sid") String messagingServiceSid,
        String sid,
        @JsonProperty("friendly_name") String friendlyName,
        @JsonProperty("unique_name") String uniqueName,
        @JsonProperty("attributes") String attributes,
        Conversation.State state,
        @JsonProperty("date_created") ZonedDateTime dateCreated,
        @JsonProperty("zone_date_time") ZonedDateTime dateUpdated,
        Map<String, Object>timers,
        URI url,
        Map<String, String> links,
        Map<String, Object> bindings
) {
}

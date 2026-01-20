package twilio_spa_fetch_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;
import java.util.Date;
import java.util.Map;

public record TaskChannelRecordDTO(
        @JsonProperty("account_sid") String accountSid,
        @JsonProperty("date_created") Date dateCreated,
        @JsonProperty("date_updated") Date dateUpdated,
        @JsonProperty("friendly_name") String friendlyName,
        String sid,
        @JsonProperty("unique_name") String uniqueName,
        URI url,
        @JsonProperty("workspace_sid") String workspaceSid,
        @JsonProperty("channel_optimized_routing") Boolean channelOptimizedRouting,
        Map<String, String> links
) {}

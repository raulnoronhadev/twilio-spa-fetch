package twilio_spa_fetch_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.twilio.rest.studio.v2.Flow;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;

public record FlowDTO(
        @JsonProperty("account_sid") String accountSid,
        String sid,
        @JsonProperty("friendly_name") String friendlyName,
        @JsonProperty("date_created") Date dateCreated,
        @JsonProperty("date_updated") Date dateUpdated,
        Flow.Status status,
        Integer revision,
        String commit_message,
        URI url,
        Boolean valid,
        List<Map<String, Object>> errors,
        List<Map<String, Object>> warnings,
        Map<String, String> links,
        Map<String, Object> definition
) {}
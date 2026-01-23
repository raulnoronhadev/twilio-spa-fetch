package twilio_spa_fetch_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.twilio.rest.taskrouter.v1.Workspace;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Map;

public record WorkspaceDTO(
        @JsonProperty("account_sid") String accountSid,
        String sid,
        @JsonProperty("friendly_name") String friendlyName,
        @JsonProperty("date_created") ZonedDateTime dateCreated,
        @JsonProperty("date_updated") ZonedDateTime dateUpdated,
        @JsonProperty("default_activity_name") String defaultActivityName,
        @JsonProperty("default_activity_sid") String defaultActivitySid,
        @JsonProperty("event_callback_url") URI eventCallbackUrl,
        @JsonProperty("events_filter") String eventsFilter,
        @JsonProperty("multi_task_enabled") Boolean multiTaskEnabled,
        @JsonProperty("timeout_activity_name") String timeoutActivityName,
        @JsonProperty("timeout_activity_sid") String timeoutActivitySid,
        @JsonProperty("prioritize_queue_order") Workspace.QueueOrder prioritizeQueueOrder,
        URI url
) {}

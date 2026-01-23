package twilio_spa_fetch_backend.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TaskRouterBackupDTO(
        String backupDate,
        String accountSid,
        Integer totalWorkspaces,
        List<WorkspaceDTO> workspaces
) {
}

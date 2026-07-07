package twilio_spa_fetch_backend.dto;

import java.time.Instant;

public record BackupFileDTO(
        String fileName,
        long size,
        Instant lastModified
) {}

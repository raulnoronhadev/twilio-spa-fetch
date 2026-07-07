package twilio_spa_fetch_backend.dto;
import lombok.Builder;

@Builder
public record LoginResponseDTO(
    String token,
    String accountSid,
    String accountName,
    long expireIn
) {}

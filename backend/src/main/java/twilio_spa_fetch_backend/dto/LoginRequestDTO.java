package twilio_spa_fetch_backend.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(

        @NotBlank(message = "Account SID is required")
        String accountSid,

        @NotBlank(message = "Auth Token is required")
        String authToken
) {}

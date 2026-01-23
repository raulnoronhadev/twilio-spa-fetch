package twilio_spa_fetch_backend.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com. twilio.type.PhoneNumber;
import java.time.ZonedDateTime;
import java. net. URI;

public record PhoneDTO(
        @JsonProperty("account_sid") String accountSid,
        @JsonProperty("friendly_name") String friendlyName,
        @JsonProperty("phone_number") PhoneNumber phoneNumber,
        @JsonProperty("phone_number_sid") String phoneNumberSid,
        @JsonProperty("identity_sid") String identitySid,
        @JsonProperty("date_created") ZonedDateTime dateCreated,
        @JsonProperty("date_updated") ZonedDateTime dateUpdated,
        String origin,
        String status,
        @JsonProperty("smsUrl") URI smsUrl,
        @JsonProperty("voice_url") URI voiceUrl
) {}

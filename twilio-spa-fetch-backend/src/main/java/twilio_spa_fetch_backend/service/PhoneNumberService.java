package twilio_spa_fetch_backend.service;

import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.IncomingPhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import twilio_spa_fetch_backend.dto.PhoneNumberDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class PhoneNumberService {

    @Value("${twilio.account-sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth-token}")
    private String AUTH_TOKEN;

    public ResponseEntity<List<PhoneNumberDTO>> listAllPhoneNumbers() {
        ResourceSet<IncomingPhoneNumber> incomingPhoneNumbers = IncomingPhoneNumber.reader().limit(50).read();
        List<PhoneNumberDTO> dtoList = new ArrayList<>();
        for (IncomingPhoneNumber phoneNumberResource : incomingPhoneNumbers) {
            PhoneNumberDTO dto = new PhoneNumberDTO(
                    phoneNumberResource.getAccountSid(),
                    phoneNumberResource.getFriendlyName(),
                    phoneNumberResource.getPhoneNumber(),
                    phoneNumberResource.getSid(),
                    phoneNumberResource.getIdentitySid(),
                    phoneNumberResource.getDateCreated(),
                    phoneNumberResource.getDateUpdated(),
                    phoneNumberResource.getOrigin(),
                    phoneNumberResource.getStatus(),
                    phoneNumberResource.getSmsUrl(),
                    phoneNumberResource.getVoiceUrl()
            );
            dtoList.add(dto);
        }
        return ResponseEntity.ok(dtoList);
    }
}

package twilio_spa_fetch_backend.service;

import java.util.List;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.IncomingPhoneNumber;
import twilio_spa_fetch_backend.dto.PhoneDTO;
import twilio_spa_fetch_backend.mapper.PhoneNumberMapper;

@Service
public class PhoneNumberService {

    @Autowired
    PhoneNumberMapper phoneNumberMapper;

    public List<PhoneDTO> getAllPhoneNumbers() {
        ResourceSet<IncomingPhoneNumber> phoneNumber = IncomingPhoneNumber.reader().read();
        List<IncomingPhoneNumber> phoneNumberList = StreamSupport.stream(phoneNumber.spliterator(), false).toList();
        return phoneNumberMapper.phoneNumberToPhoneNumberDTOList(phoneNumberList);
    }
}

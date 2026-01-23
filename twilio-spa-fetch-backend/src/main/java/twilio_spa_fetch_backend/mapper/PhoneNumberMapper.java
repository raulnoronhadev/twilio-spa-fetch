package twilio_spa_fetch_backend.mapper;

import com.twilio.rest.api.v2010.account.IncomingPhoneNumber;
import org.mapstruct.Mapper;
import twilio_spa_fetch_backend.dto.PhoneDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PhoneNumberMapper {

    List<PhoneDTO> phoneNumberToPhoneNumberDTOList(List<IncomingPhoneNumber> phoneNumber);

}

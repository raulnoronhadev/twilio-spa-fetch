package twilio_spa_fetch_backend.mapper;

import com.twilio.rest.api.v2010.account.IncomingPhoneNumber;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import twilio_spa_fetch_backend.dto.PhoneDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PhoneNumberMapper {

    @Mapping(source = "sid", target = "phoneNumberSid")
    PhoneDTO phoneNumberToPhoneNumberDTO(IncomingPhoneNumber phoneNumber);

    List<PhoneDTO> phoneNumberToPhoneNumberDTOList(List<IncomingPhoneNumber> phoneNumber);

}

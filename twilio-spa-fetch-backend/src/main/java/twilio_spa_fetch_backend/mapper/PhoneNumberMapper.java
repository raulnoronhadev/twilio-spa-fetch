package twilio_spa_fetch_backend.mapper;

import com.twilio.rest.api.v2010.account.IncomingPhoneNumber;
import org.mapstruct.Mapper;
import twilio_spa_fetch_backend.dto.PhoneResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PhoneNumberMapper {

    List<PhoneResponse> phoneNumberToPhoneNumberDTOList(List<IncomingPhoneNumber> phoneNumber);

}

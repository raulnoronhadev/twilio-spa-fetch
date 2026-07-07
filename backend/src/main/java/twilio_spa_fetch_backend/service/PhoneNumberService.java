package twilio_spa_fetch_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.twilio.base.Page;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.Domains;
import com.twilio.rest.api.v2010.account.IncomingPhoneNumber;
import twilio_spa_fetch_backend.dto.PageDTO;
import twilio_spa_fetch_backend.dto.PhoneDTO;
import twilio_spa_fetch_backend.mapper.PhoneNumberMapper;
import twilio_spa_fetch_backend.security.TwilioClientProvider;
import twilio_spa_fetch_backend.util.PageTokenCodec;

import java.util.List;

@Service
public class PhoneNumberService {

    @Autowired
    PhoneNumberMapper phoneNumberMapper;

    @Autowired
    TwilioClientProvider twilioClientProvider;

    public PageDTO<PhoneDTO> getPhoneNumbers(int pageSize, String pageToken) {
        TwilioRestClient client = twilioClientProvider.getClient();
        var reader = IncomingPhoneNumber.reader().pageSize(Math.clamp(pageSize, 1, 100));
        Page<IncomingPhoneNumber> page = pageToken == null
                ? reader.firstPage(client)
                : reader.getPage(PageTokenCodec.decode(pageToken), client);
        List<PhoneDTO> items = phoneNumberMapper.phoneNumberToPhoneNumberDTOList(page.getRecords());
        String nextToken = page.hasNextPage()
                ? PageTokenCodec.encode(page.getNextPageUrl(Domains.API.toString()))
                : null;
        return new PageDTO<>(items, nextToken);
    }
}

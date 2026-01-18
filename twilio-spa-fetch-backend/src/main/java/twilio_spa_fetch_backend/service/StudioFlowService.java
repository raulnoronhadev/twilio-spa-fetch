package twilio_spa_fetch_backend.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.studio.v2.Flow;
import com.twilio.base.ResourceSet;
import twilio_spa_fetch_backend.dto.FlowRecordDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudioFlowService {

    @Value("${twilio.account-sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth-token}")
    private String AUTH_TOKEN;

    public ResponseEntity<List<FlowRecordDTO>> findAllFlows() {
        ResourceSet<Flow> flows = Flow.reader().read();

        List<FlowRecordDTO> dtoList = new ArrayList<>();

        for (Flow flowResource : flows) {
            String flowSid = flowResource.getSid();
            FlowRecordDTO dto = new FlowRecordDTO(
                    flowResource.getAccountSid(),
                    flowSid,
                    flowResource.getFriendlyName(),
                    java.util.Date.from(flowResource.getDateCreated().toInstant()),
                    flowResource.getDateUpdated() != null ? java.util.Date.from(flowResource.getDateUpdated().toInstant()) : null,
                    flowResource.getStatus().toString(),
                    flowResource.getRevision(),
                    flowResource.getCommitMessage(),
                    flowResource.getUrl().toString(),
                    flowResource.getValid(),
                    flowResource.getErrors(),
                    flowResource.getWarnings(),
                    flowResource.getLinks(),
                    getDefinitionBySid(flowSid)
            );
            dtoList.add(dto);
        }

        return ResponseEntity.ok(dtoList);
    }

    public ResponseEntity<FlowRecordDTO> getFlowBySid(String flowSid) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Flow flowResource = Flow.fetcher(flowSid).fetch();
        FlowRecordDTO dto = new FlowRecordDTO(
                flowResource.getAccountSid(),
                flowResource.getSid(),
                flowResource.getFriendlyName(),
                java.util.Date.from(flowResource.getDateCreated().toInstant()),
                flowResource.getDateUpdated() != null ? java.util.Date.from(flowResource.getDateUpdated().toInstant()) : null,
                flowResource.getStatus().toString(),
                flowResource.getRevision(),
                flowResource.getCommitMessage(),
                flowResource.getUrl().toString(),
                flowResource.getValid(),
                flowResource.getErrors(),
                flowResource.getWarnings(),
                flowResource.getLinks(),
                flowResource.getDefinition()
        );
        return ResponseEntity.ok(dto);
    }

    public Object getDefinitionBySid(String flowSid) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Flow record = Flow.fetcher(flowSid).fetch();
        return record.getDefinition();
    }
}

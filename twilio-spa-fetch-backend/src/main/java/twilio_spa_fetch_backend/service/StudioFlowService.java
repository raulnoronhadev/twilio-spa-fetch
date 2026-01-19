package twilio_spa_fetch_backend.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.studio.v2.Flow;
import com.twilio.base.ResourceSet;
import twilio_spa_fetch_backend.dto.FlowDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudioFlowService {

    public ResponseEntity<List<FlowDTO>> findAllFlows() {
        ResourceSet<Flow> flows = Flow.reader().read();

        List<FlowDTO> dtoList = new ArrayList<>();

        for (Flow flowResource : flows) {
            String flowSid = flowResource.getSid();
            FlowDTO dto = new FlowDTO(
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

    public ResponseEntity<FlowDTO> getFlowBySid(String flowSid) {
        Flow flowResource = Flow.fetcher(flowSid).fetch();
        FlowDTO dto = new FlowDTO(
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
        Flow record = Flow.fetcher(flowSid).fetch();
        return record.getDefinition();
    }
}

package twilio_spa_fetch_backend.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.twilio.rest.studio.v2.Flow;
import com.twilio.base.ResourceSet;
import twilio_spa_fetch_backend.dto.FlowRecordDTO;
import twilio_spa_fetch_backend.mapper.StudioMapper;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class StudioFlowService {

    @Autowired
    private StudioMapper studioMapper;

    public FlowRecordDTO getFlowBySid(String flowSid) {
        Flow flow = Flow.fetcher(flowSid).fetch();
        return studioMapper.flowToFlowRecordDTO(flow);
    }

    public List<FlowRecordDTO> getAllFlows() {
        ResourceSet<Flow> flows = Flow.reader().read();
        return StreamSupport.stream(flows.spliterator(), false).map(flowSummary -> {
            Flow fullFlow = Flow.fetcher(flowSummary.getSid()).fetch();
            return studioMapper.flowToFlowRecordDTO(fullFlow);
        }).toList();
    }

    public Object getDefinitionBySid(String flowSid) {
        Flow flow = Flow.fetcher(flowSid).fetch();
        return flow.getDefinition();
    }
}

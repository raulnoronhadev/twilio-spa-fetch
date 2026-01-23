package twilio_spa_fetch_backend.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.rest.studio.v2.FlowCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.twilio.rest.studio.v2.Flow;
import com.twilio.base.ResourceSet;
import twilio_spa_fetch_backend.dto.FlowResponse;
import twilio_spa_fetch_backend.mapper.StudioMapper;
import twilio_spa_fetch_backend.ports.StoragePort;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

@Service
public class StudioFlowService {

    @Autowired
    private StudioMapper studioMapper;

    @Autowired
    private StoragePort storagePort;

    @Autowired
    private ObjectMapper objectMapper;

    public FlowResponse getFlowBySid(String flowSid) {
        Flow flow = Flow.fetcher(flowSid).fetch();
        return studioMapper.flowToFlowDTO(flow);
    }

    public List<FlowResponse> getAllFlows() {
        ResourceSet<Flow> flows = Flow.reader().read();
        return StreamSupport.stream(flows.spliterator(), false).map(flowSummary -> {
            Flow fullFlow = Flow.fetcher(flowSummary.getSid()).fetch();
            return studioMapper.flowToFlowDTO(fullFlow);
        }).toList();
    }

    public Object getDefinitionBySid(String flowSid) {
        Flow flow = Flow.fetcher(flowSid).fetch();
        return flow.getDefinition();
    }

    public String backupFlowBySid(String flowSid) {
        try {
            FlowResponse flowResponse = getFlowBySid(flowSid);
            String jsonContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(flowResponse);
            byte[] jsonBytes = jsonContent.getBytes("UTF-8");
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String fileName = String.format("flows/%s_%s.json", flowSid, timestamp);
            return storagePort.uploadFile(jsonBytes, fileName, "application/json");
        } catch (Exception e) {
            throw new RuntimeException("Flow backup error: " + e.getMessage(), e);
        }
    }

    public List<String> backupAllFlows() {
        List<FlowResponse> allFlows = getAllFlows();

        return allFlows.stream().map(flow -> {
            try {
                String jsonContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(flow);
                byte[] jsonBytes = jsonContent.getBytes("UTF-8");
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
                String fileName = String.format("flows/%s_%s.json", flow.sid(), timestamp);
                return storagePort.uploadFile(jsonBytes, fileName, "application/json");
            } catch (Exception e) {
                throw new RuntimeException("Backup error: " + e.getMessage(), e);
            }
        }).toList();
    }

    public String restoreDeletedFlow(String fileName) {
        try {
            byte[] jsonBytes = storagePort.downloadFile(fileName);
            String jsonContent = new String(jsonBytes, "UTF-8");
            FlowResponse flowResponse = objectMapper.readValue(jsonContent, FlowResponse.class);
            String friendlyName = flowResponse.friendlyName();
            Flow.Status status = flowResponse.status();
            Map<String, Object> definition = flowResponse.definition();
            FlowCreator flowCreator = Flow.creator(friendlyName, status, definition);
            Flow newFlow = flowCreator.create();
            System.out.print("Flow restored successfully: " + newFlow.getSid());
            return newFlow.getSid();
        } catch (Exception e) {
            throw new RuntimeException("Error restoring flow: " + e.getMessage(), e);
        }
    }

}

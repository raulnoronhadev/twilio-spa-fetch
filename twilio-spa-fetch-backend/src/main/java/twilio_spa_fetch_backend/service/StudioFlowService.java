package twilio_spa_fetch_backend.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.base.Page;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.Domains;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.twilio.rest.studio.v2.Flow;
import com.twilio.base.ResourceSet;
import twilio_spa_fetch_backend.dto.FlowDTO;
import twilio_spa_fetch_backend.dto.PageDTO;
import twilio_spa_fetch_backend.mapper.StudioMapper;
import twilio_spa_fetch_backend.ports.StoragePort;
import twilio_spa_fetch_backend.security.TwilioClientProvider;
import twilio_spa_fetch_backend.util.PageTokenCodec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class StudioFlowService {

    @Autowired
    private StudioMapper studioMapper;

    @Autowired
    private StoragePort storagePort;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TwilioClientProvider twilioClientProvider;

    public FlowDTO getFlowBySid(String flowSid) {
        Flow flow = Flow.fetcher(flowSid).fetch(twilioClientProvider.getClient());
        return studioMapper.flowToFlowDTO(flow);
    }

    public PageDTO<FlowDTO> getFlows(int pageSize, String pageToken) {
        TwilioRestClient client = twilioClientProvider.getClient();
        var reader = Flow.reader().pageSize(Math.clamp(pageSize, 1, 100));
        Page<Flow> page = pageToken == null
                ? reader.firstPage(client)
                : reader.getPage(PageTokenCodec.decode(pageToken), client);
        List<FlowDTO> items = page.getRecords().stream().map(studioMapper::flowToFlowDTO).toList();
        String nextToken = page.hasNextPage()
                ? PageTokenCodec.encode(page.getNextPageUrl(Domains.STUDIO.toString()))
                : null;
        return new PageDTO<>(items, nextToken);
    }

    // Unpaginated variant kept for backups, which need every flow with its definition.
    public List<FlowDTO> getAllFlows() {
        TwilioRestClient client = twilioClientProvider.getClient();
        ResourceSet<Flow> flows = Flow.reader().read(client);
        return StreamSupport.stream(flows.spliterator(), false).map(flowSummary -> {
            Flow fullFlow = Flow.fetcher(flowSummary.getSid()).fetch(client);
            return studioMapper.flowToFlowDTO(fullFlow);
        }).toList();
    }

    public Object getDefinitionBySid(String flowSid) {
        Flow flow = Flow.fetcher(flowSid).fetch(twilioClientProvider.getClient());
        return flow.getDefinition();
    }

    public String backupFlowBySid(String flowSid) {
        FlowDTO flowDTO = getFlowBySid(flowSid);
        return uploadFlowBackup(flowDTO);
    }

    public List<String> backupAllFlows() {
        return getAllFlows().stream().map(this::uploadFlowBackup).toList();
    }

    public String restoreDeletedFlow(String fileName) {
        byte[] jsonBytes = storagePort.downloadFile(fileName);
        FlowDTO flowDTO = parseFlowBackup(jsonBytes, fileName);
        Flow newFlow = Flow.creator(flowDTO.friendlyName(), Flow.Status.forValue(flowDTO.status()), flowDTO.definition())
                .create(twilioClientProvider.getClient());
        return newFlow.getSid();
    }

    private String uploadFlowBackup(FlowDTO flow) {
        byte[] jsonBytes = serializeFlow(flow);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String fileName = String.format("flows/%s_%s.json", flow.sid(), timestamp);
        return storagePort.uploadFile(jsonBytes, fileName, "application/json");
    }

    private byte[] serializeFlow(FlowDTO flow) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(flow)
                    .getBytes(StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize flow " + flow.sid(), e);
        }
    }

    private FlowDTO parseFlowBackup(byte[] jsonBytes, String fileName) {
        try {
            return objectMapper.readValue(jsonBytes, FlowDTO.class);
        } catch (IOException e) {
            throw new IllegalStateException("Backup file is not a valid flow backup: " + fileName, e);
        }
    }
}

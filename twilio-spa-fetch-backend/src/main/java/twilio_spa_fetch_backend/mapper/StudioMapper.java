package twilio_spa_fetch_backend.mapper;
import com.twilio.rest.studio.v2.Flow;
import org.mapstruct.Mapper;
import twilio_spa_fetch_backend.dto.FlowRecordDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudioMapper {

    FlowRecordDTO flowToFlowRecordDTO(Flow flow);
    List<FlowRecordDTO> flowToFlowRecordDTOList(List<Flow> flow);

}

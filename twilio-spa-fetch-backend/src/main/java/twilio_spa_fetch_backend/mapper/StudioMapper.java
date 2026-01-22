package twilio_spa_fetch_backend.mapper;
import com.twilio.rest.studio.v2.Flow;
import org.mapstruct.Mapper;
import twilio_spa_fetch_backend.dto.FlowResponse;

@Mapper(componentModel = "spring")
public interface StudioMapper {

    FlowResponse flowToFlowDTO(Flow flow);

}

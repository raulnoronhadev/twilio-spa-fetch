package twilio_spa_fetch_backend.mapper;
import com.twilio.rest.studio.v2.Flow;
import org.mapstruct.Mapper;
import twilio_spa_fetch_backend.dto.FlowDTO;

@Mapper(componentModel = "spring")
public interface StudioMapper {
    FlowDTO flowToFlowDTO(Flow flow);

    // Twilio enums expose their wire value ("published") via toString();
    // name() would leak the Java constant ("PUBLISHED") into the API contract.
    default String map(Flow.Status status) {
        return status == null ? null : status.toString();
    }
}

package das.tools.notifier.notify.entitys.response.mx;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MxResponse extends Mx {
    @JsonProperty("event_id")
    private String eventId;
    @JsonProperty("content_uri")
    private String contentUri;
}

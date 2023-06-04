package das.tools.notifier.notify.entitys.response.vb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViberBroadcastMessageResponse {
    private int status;
    @JsonProperty("status_message")
    private String statusMessage;
}

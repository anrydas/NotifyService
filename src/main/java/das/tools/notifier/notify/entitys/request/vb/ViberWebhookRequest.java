package das.tools.notifier.notify.entitys.request.vb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViberWebhookRequest {
    private String event;
    private long timestamp;
    @JsonProperty("message_token")
    private String messageToken;
    private boolean subscribed;
    private ViberUserInfo userInfo;
}

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
public class ViberSendBroadcastMessageRequest {
    @JsonProperty("min_api_version")
    @Builder.Default
    private int minApiVersion = 1;
    @JsonProperty("tracking_data")
    private String trackingData;
    @Builder.Default
    private ViberMessageType type = ViberMessageType.text;
    private String text;
    @JsonProperty("broadcast_list")
    private String[] broadcastList;
    private String media;
    private long size;
}

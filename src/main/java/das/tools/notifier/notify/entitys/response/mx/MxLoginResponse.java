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
public class MxLoginResponse extends Mx {
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("home_server")
    private String homeServer;
    @JsonProperty("device_id")
    private String deviceId;
}

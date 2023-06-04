package das.tools.notifier.notify.entitys.request.mx.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MxLogin {
    private MxIdentifier identifier;

    @JsonProperty("initial_device_display_name")
    @Builder.Default
    private String deviceName = "Das Notifier Devise #1";

    private String password;

    @Builder.Default
    private String type = "m.login.password";
}

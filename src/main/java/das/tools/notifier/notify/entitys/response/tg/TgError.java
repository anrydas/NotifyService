package das.tools.notifier.notify.entitys.response.tg;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TgError extends Tg {
    private boolean ok;
    @JsonProperty("error_code")
    private int errorCode;
    private String description;
}

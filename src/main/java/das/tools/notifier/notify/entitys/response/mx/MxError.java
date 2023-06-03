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
public class MxError extends Mx {
    @JsonProperty("errcode")
    private String errCode;
    private String error;
}

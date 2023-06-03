package das.tools.notifier.notify.entitys.response.tg;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TgChat {
    private long id;
    private String type;
    @JsonProperty("first_name")
    private String firstName;
    private String username;
}

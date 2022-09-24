package das.tools.notifier.notify.entitys.response.tg;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TgResult {
    @JsonProperty("message_id")
    private long messageId;
    private long date;
    private String text;
    private TgFrom from;
    private TgChat chat;
}

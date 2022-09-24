package das.tools.notifier.notify.entitys.request.tg;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TgSendMessage {
    @JsonProperty("chat_id")
    private String chatId;
    private String text;
    @JsonProperty("parse_mode")
    @Builder.Default
    private String parseMode = "markdown";
}

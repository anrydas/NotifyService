package das.tools.notifier.notify.entitys.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Request {
    private MessengerType messenger;
    @JsonProperty("chat")
    private String chatId;
    private String subject;
    private String message;
    private String file;
}

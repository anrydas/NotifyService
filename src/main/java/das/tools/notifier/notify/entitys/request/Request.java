package das.tools.notifier.notify.entitys.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import das.tools.notifier.notify.entitys.response.UploadFileInfo;
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
    @JsonProperty("file_info")
    private UploadFileInfo fileInfo;
}

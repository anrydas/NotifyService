package das.tools.notifier.notify.entitys.request.mx;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatrixSendMessage {
    private String body;

    @Builder.Default
    private String format = "org.matrix.custom.html";

    @JsonProperty("formatted_body")
    private String formattedBody;

    @JsonProperty("msgtype")
    @Builder.Default
    private String msgType = "m.text";
    private String url;
    private FileInfo info;
}

package das.tools.notifier.notify.entitys.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class UploadFileInfo {
    @JsonProperty("name")
    private String fileName;
    @JsonProperty("url")
    private String fileUrl;
    @JsonProperty("size")
    private long fileSize;
    @JsonProperty("mime")
    private String fileMime;
}

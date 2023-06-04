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
public class FileInfo {
    int h;
    int w;
    long size;
    @JsonProperty("mimetype")
    String mimeType;
}

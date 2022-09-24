package das.tools.notifier.notify.entitys.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationResponse {
    private ResponseStatus status;
    private String comment;
    @JsonProperty("error_message")
    private String errorMessage;
    @JsonProperty("file_info")
    private UploadFileInfo fileInfo;
}

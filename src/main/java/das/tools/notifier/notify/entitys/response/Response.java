package das.tools.notifier.notify.entitys.response;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Response {
    private ResponseStatus status;
    private String comment;
    private String errorMessage;
}

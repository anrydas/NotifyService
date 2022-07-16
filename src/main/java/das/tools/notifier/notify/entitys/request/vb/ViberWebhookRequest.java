package das.tools.notifier.notify.entitys.request.vb;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViberWebhookRequest {
    private String event;
    private long timestamp;
    private String message_token;
}

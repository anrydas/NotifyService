package das.tools.notifier.notify.entitys.request.mx.login;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MxIdentifier {
    @Builder.Default
    private String type = "m.id.user";
    private String user;
}

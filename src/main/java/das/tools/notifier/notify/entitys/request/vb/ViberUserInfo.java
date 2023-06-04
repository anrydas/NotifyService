package das.tools.notifier.notify.entitys.request.vb;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViberUserInfo {
    private String id;
    private String country;
    private String language;
    private String name;
}

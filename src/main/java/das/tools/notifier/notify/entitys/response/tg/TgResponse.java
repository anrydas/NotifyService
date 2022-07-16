package das.tools.notifier.notify.entitys.response.tg;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class TgResponse extends Tg {
    private boolean ok;
    private TgResult result;
}

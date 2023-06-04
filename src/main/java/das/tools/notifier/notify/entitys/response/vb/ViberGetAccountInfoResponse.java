package das.tools.notifier.notify.entitys.response.vb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViberGetAccountInfoResponse {
    private String id;
    private String uri;
    private String name;
    private String webhook;
    private ViberMemberResponse[] members;
    @JsonProperty("subscribers_count")
    private int subscribersCount;
}

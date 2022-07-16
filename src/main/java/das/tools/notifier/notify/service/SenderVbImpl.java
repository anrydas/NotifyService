package das.tools.notifier.notify.service;

import das.tools.notifier.notify.entitys.request.Request;
import das.tools.notifier.notify.entitys.response.Response;
import das.tools.notifier.notify.entitys.response.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("viber")
@Slf4j
public class SenderVbImpl implements Sender {
    private final RestTemplate restTemplate;

    public SenderVbImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Response send(Request request) {
        Response res;
        //TODO: Add Send Message to Viber logic
        res = Response.builder()
                .status(ResponseStatus.OK)
                .build();
        return res;
    }
}

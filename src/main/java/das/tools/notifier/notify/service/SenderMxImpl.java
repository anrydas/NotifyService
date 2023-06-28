package das.tools.notifier.notify.service;

import das.tools.notifier.notify.entitys.request.Request;
import das.tools.notifier.notify.entitys.response.ApplicationResponse;
import das.tools.notifier.notify.entitys.response.ResponseStatus;
import das.tools.notifier.notify.entitys.response.mx.MxResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("matrix")
@Slf4j
public class SenderMxImpl implements Sender {
    private final MatrixApiImpl matrixClient;

    public SenderMxImpl(MatrixApiImpl matrixClient) {
        this.matrixClient = matrixClient;
    }

    @SneakyThrows
    @Override
    public ApplicationResponse send(Request request) {
        MxResponse response = matrixClient.sendMessage(request);
        if (log.isDebugEnabled()) log.debug("[SenderMxImpl.send] got response={}", response);
        return ApplicationResponse.builder()
                .status(ResponseStatus.OK)
                .build();
    }

}

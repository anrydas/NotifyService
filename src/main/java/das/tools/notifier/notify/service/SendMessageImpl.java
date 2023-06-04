package das.tools.notifier.notify.service;

import das.tools.notifier.notify.entitys.request.MessengerType;
import das.tools.notifier.notify.entitys.request.Request;
import das.tools.notifier.notify.entitys.response.ApplicationResponse;
import das.tools.notifier.notify.entitys.response.ResponseStatus;
import das.tools.notifier.notify.exceptions.WrongRequestParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SendMessageImpl implements SendMessage {
    private final Map<MessengerType, Sender> messageSenders = new HashMap<>();
    private final Sender messageTg;
    private final Sender messageVb;
    private final Sender messageMx;
    private final Sender messageEmail;

    @Autowired
    public SendMessageImpl(@Qualifier("telegram") Sender messageTg,
                           @Qualifier("viber") Sender messageVb,
                           @Qualifier("matrix") Sender messageMx,
                           @Qualifier("email") Sender messageEmail) {
        this.messageTg = messageTg;
        this.messageVb = messageVb;
        this.messageMx = messageMx;
        this.messageEmail = messageEmail;
    }

    @PostConstruct
    public void createMessengersMap(){
        messageSenders.put(MessengerType.TELEGRAM, messageTg);
        messageSenders.put(MessengerType.VIBER, messageVb);
        messageSenders.put(MessengerType.MATRIX, messageMx);
        messageSenders.put(MessengerType.EMAIL, messageEmail);
    }

    @Override
    public ApplicationResponse execute(MessengerType type, Request request) {
        Sender sender = messageSenders.get(type);
        ApplicationResponse res;
        try {
            res = sender.send(request);
        } catch (WrongRequestParameterException e) {
            log.error("Send message error", e);
            res = ApplicationResponse.builder()
                    .status(ResponseStatus.ERROR)
                    .errorMessage("Error occurred during sending message: " + e.getLocalizedMessage())
                    .build();
        }
        return res;
    }
}

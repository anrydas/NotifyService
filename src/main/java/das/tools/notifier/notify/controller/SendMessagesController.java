package das.tools.notifier.notify.controller;

import das.tools.notifier.notify.entitys.request.MessengerType;
import das.tools.notifier.notify.entitys.request.Request;
import das.tools.notifier.notify.entitys.response.ApplicationResponse;
import das.tools.notifier.notify.exceptions.WrongRequestParameterException;
import das.tools.notifier.notify.service.SendMessage;
import das.tools.notifier.notify.service.Utils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("${server.api.prefix}/api/v1")
public class SendMessagesController {

    private final SendMessage sendMessage;

    public SendMessagesController(SendMessage sendMessage) {
        this.sendMessage = sendMessage;
    }

    //http://localhost:23445/api/v1/send
    @SneakyThrows
    @RequestMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody Request request) {
        MessengerType type = request.getMessenger();
        if (type == null || !MessengerType.contains(type)) {
            throw new WrongRequestParameterException("Unknown messenger type");
        }
        ApplicationResponse response;
        if (log.isDebugEnabled()) log.debug("Try to process Request {}", Utils.linearizedString(request));
        response = sendMessage.execute(type, request);

        if (log.isDebugEnabled()) log.debug("response={}", Utils.linearizedString(response));
        return ResponseEntity.ok(response);
    }
}

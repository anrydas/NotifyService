package das.tools.notifier.notify.service;

import das.tools.notifier.notify.entitys.request.MessengerType;
import das.tools.notifier.notify.entitys.request.Request;
import das.tools.notifier.notify.entitys.response.Response;

public interface SendMessage {
    Response execute(MessengerType type, Request request);
}

package das.tools.notifier.notify.service;

import das.tools.notifier.notify.entitys.request.MessengerType;
import das.tools.notifier.notify.entitys.request.Request;
import das.tools.notifier.notify.entitys.response.ApplicationResponse;

public interface SendMessage {
    ApplicationResponse execute(MessengerType type, Request request);
}

package das.tools.notifier.notify.service;

import das.tools.notifier.notify.entitys.request.Request;
import das.tools.notifier.notify.entitys.response.Response;
import das.tools.notifier.notify.exceptions.WrongRequestParameterException;

public interface Sender {
    String SEND_MESSAGE_ERROR = "Error occurred while send message to %s: %s";

    Response send(Request request) throws WrongRequestParameterException;
}

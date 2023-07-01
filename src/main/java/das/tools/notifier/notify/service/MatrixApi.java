package das.tools.notifier.notify.service;

import das.tools.notifier.notify.entitys.request.Request;
import das.tools.notifier.notify.entitys.request.mx.MatrixSendMessage;
import das.tools.notifier.notify.entitys.response.mx.MxResponse;
import das.tools.notifier.notify.exceptions.WrongRequestParameterException;

public interface MatrixApi {
    MxResponse sendMessage(Request request) throws WrongRequestParameterException;

    MxResponse sendTextMessage(String roomId, String message);

    MxResponse sendFileMessage(String roomId, MatrixSendMessage request);
}

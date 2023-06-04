package das.tools.notifier.notify.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import das.tools.notifier.notify.entitys.img.ImageWH;
import das.tools.notifier.notify.entitys.img.WH;
import das.tools.notifier.notify.entitys.request.Request;
import das.tools.notifier.notify.entitys.request.mx.FileInfo;
import das.tools.notifier.notify.entitys.request.mx.MatrixSendMessage;
import das.tools.notifier.notify.entitys.response.ApplicationResponse;
import das.tools.notifier.notify.entitys.response.ResponseStatus;
import das.tools.notifier.notify.service.factory.MatrixMessageTypeFactory;
import io.kamax.matrix.client.MatrixPasswordCredentials;
import io.kamax.matrix.client._MatrixClient;
import io.kamax.matrix.hs._MatrixRoom;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component("matrix")
@Slf4j
public class SenderMxImpl implements Sender {
    @Value("${mx.baseUrl}")
    private String mxBaseUrl;
    @Value("${mx.bot.default.roomid}")
    private String defRoomId;
    @Value("${mx.bot.user}")
    private String mxUser;
    @Value("${mx.bot.pass}")
    private String mxPassword;

    private final _MatrixClient matrixClient;

    public SenderMxImpl(_MatrixClient matrixClient) {
        this.matrixClient = matrixClient;
    }

    @SneakyThrows
    @Override
    public ApplicationResponse send(Request request) {
        if (matrixClient.getUser() == null) {
            matrixClient.login(new MatrixPasswordCredentials(mxUser, mxPassword));
        }

        String resId = null;
        FileInfo fileInfo = null;
        String mimeType = "";
        String fileName = "";
        String roomId = Utils.getNotNullStringOrDefault(request.getChatId(), defRoomId);
        String file = Utils.getNotNullString(request.getFile());
        if (!"".equals(file)) {
            File f = new File(file);
            fileName = f.getName();
            if (!f.exists()) {
                log.error("File '{}' not exists", file);
            } else {
                mimeType = Utils.getMimeType(file);
                if (log.isDebugEnabled()) log.debug("[Mx send] got mimeType='{}' for file='{}'", mimeType, file);
                if (mimeType.startsWith("image")) {
                    WH wh = ImageWH.getInstance().calculateFor(f);
                    fileInfo = FileInfo.builder()
                            .size(f.length())
                            .mimeType(mimeType)
                            .h(wh.getH())
                            .w(wh.getW())
                            .build();
                } else {
                    fileInfo = FileInfo.builder()
                            .size(f.length())
                            .mimeType(mimeType)
                            .h(0).w(0).build();
                }
                resId = matrixClient.putMedia(f, mimeType, file);
                if (log.isDebugEnabled()) log.debug("[Mx send] got resource URL={}", resId);
            }
        }
        _MatrixRoom room = matrixClient.getRoom(roomId);
        String event = MatrixMessageTypeFactory.get(mimeType, resId);
        if (log.isDebugEnabled()) log.debug("[Mx send] got message event={}", event);
        String message = request.getMessage();
        if (log.isDebugEnabled()) log.debug("[Mx send] fileName={}", fileName);
        JsonObject jsonObject = getJsonMessage(event, fileInfo, resId, message, fileName);
        room.sendEvent("m.room.message", jsonObject);
        String comment = (!"".equals(file)) ? String.format("File URL='%s' loaded into Room ID='%s'",  resId, roomId) : null;
        return ApplicationResponse.builder()
                .status(ResponseStatus.OK)
                .comment(comment)
                .build();
    }

    private JsonObject getJsonMessage(String messageType, FileInfo fileInfo, String url, String message, String fileName) {
        MatrixSendMessage content;
        if ("m.text".equals(messageType)) {
            content = MatrixSendMessage.builder()
                    .msgType(messageType)
                    .info(fileInfo)
                    .url(url)
                    .body(message)
                    .formattedBody(message)
                    .build();
        } else {
            content = MatrixSendMessage.builder()
                    .msgType(messageType)
                    .info(fileInfo)
                    .url(url)
                    .body(String.format("[%s] - %s", fileName, Utils.removeHtmlTags(message)))
                    .build();
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = null;
        try {
            jsonStr = mapper.writeValueAsString(content);
        } catch (JsonProcessingException e) {
            log.error("Error getting JSON", e);
        }
        if (log.isDebugEnabled()) log.debug("[getJsonMessage] got Json message={}", jsonStr);
        return (new JsonParser()).parse(jsonStr).getAsJsonObject();
    }
}

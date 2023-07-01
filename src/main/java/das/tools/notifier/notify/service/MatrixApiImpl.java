package das.tools.notifier.notify.service;

import das.tools.notifier.notify.entitys.img.ImageWH;
import das.tools.notifier.notify.entitys.img.WH;
import das.tools.notifier.notify.entitys.request.Request;
import das.tools.notifier.notify.entitys.request.mx.FileInfo;
import das.tools.notifier.notify.entitys.request.mx.MatrixSendMessage;
import das.tools.notifier.notify.entitys.request.mx.login.MxIdentifier;
import das.tools.notifier.notify.entitys.request.mx.login.MxLogin;
import das.tools.notifier.notify.entitys.response.mx.MxLoginResponse;
import das.tools.notifier.notify.entitys.response.mx.MxResponse;
import das.tools.notifier.notify.exceptions.WrongRequestParameterException;
import das.tools.notifier.notify.service.factory.MatrixMessageTypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class MatrixApiImpl implements MatrixApi {
    private static final int DEFAULT_LOGIN_INTERVAL = 60*60;
    private final RestTemplate restTemplate;

    @Value("${mx.bot.default.roomid}")
    private String defRoomId;
    @Value("${mx.bot.user}")
    private String userName;
    @Value("${mx.bot.pass}")
    private String userPass;
    @Value("${mx.baseUrl}")
    private String baseUrl;
    @Value("${mx.session.lifetime}")
    private int sessionLifetime = 24;
    private String accessToken = "";
    private Instant lastLoginTime;

    public MatrixApiImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private void login() {
        if (log.isDebugEnabled()) log.debug("[MatrixApiImpl.login] Try to login");
        String url = ServletUriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/_matrix/client/v3/login")
                .toUriString();
        MxLogin request = MxLogin.builder()
                .identifier(MxIdentifier.builder()
                        .user(userName)
                        .build())
                .password(userPass)
                .build();
        HttpEntity<MxLogin> httpEntity = new HttpEntity<>(request, getMessageHeaders());
        ResponseEntity<MxLoginResponse> response = restTemplate.exchange(url,
                HttpMethod.POST,
                httpEntity,
                MxLoginResponse.class);
        MxLoginResponse body = response.getBody();
        if (log.isDebugEnabled()) log.debug("[MatrixApiImpl.login] got body={}", body);
        this.lastLoginTime = Instant.now();
        this.accessToken = body != null && body.getAccessToken() != null ? body.getAccessToken() : "";
    }

    @Override
    public MxResponse sendMessage(Request request) throws WrongRequestParameterException {
        if (log.isDebugEnabled()) log.debug("[MatrixApiImpl.sendMessage] Try to process request {}", Utils.linearizedString(request));
        if (!isConnected()) {
            login();
        }
        FileInfo fileInfo = null;
        String roomId = Utils.getNotNullStringOrDefault(request.getChatId(), defRoomId);
        String file = Utils.getNotNullString(request.getFile());
        String message = request.getMessage();

        String mimeType = "";
        String fileName = "";
        MxResponse response;
        if (!"".equals(file)) {
            File f = new File(file);
            fileName = f.getName();
            if (log.isDebugEnabled()) log.debug("[MatrixApiImpl.sendMessage] got fileName={}", fileName);
            if (!f.exists()) {
                log.error("File '{}' not exists. Sending message without file", file);
                response = sendTextMessage(roomId, message);
            } else {
                mimeType = Utils.getMimeType(file);
                if (log.isDebugEnabled()) log.debug("[MatrixApiImpl.sendMessage] got mimeType='{}' for file='{}'", mimeType, file);
                if (Utils.isImageFile(fileName)) {
                    WH wh = ImageWH.getInstance().calculateFor(f);
                    fileInfo = FileInfo.builder()
                            .size(f.length())
                            .mimeType(mimeType)
                            .h(wh.getH()).w(wh.getW())
                            .build();
                } else {
                    fileInfo = FileInfo.builder()
                            .size(f.length())
                            .mimeType(mimeType)
                            .h(0).w(0).build();
                }
                if (log.isDebugEnabled()) log.debug("[MatrixApiImpl.sendMessage] got fileInfo={}", fileInfo);

                String resId = postFile(file);
                if (log.isDebugEnabled()) log.debug("[MatrixApiImpl.sendMessage] got resId={}", resId);
                MatrixSendMessage sendFileRequest = MatrixSendMessage.builder()
                        .msgType(MatrixMessageTypeFactory.get(mimeType, resId))
                        .url(resId)
                        .info(fileInfo)
                        .body(String.format("[%s] - %s", fileName, Utils.removeHtmlTags(message)))
                        .formattedBody(message)
                        .build();
                response = sendFileMessage(roomId, sendFileRequest);
            }
        } else {
            response = sendTextMessage(roomId, message);
        }

        if (log.isDebugEnabled()) log.debug("[MatrixApiImpl.sendMessage] got body={}", response);
        return response;
    }

    @Override
    public MxResponse sendTextMessage(String roomId, String message) {
        String url = ServletUriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/_matrix/client/v3/rooms/")
                .path(roomId)
                .path("/send/m.room.message/")
                .path(getUniqueId())
                .queryParam("access_token", accessToken)
                .toUriString();
        MatrixSendMessage mxRequest = MatrixSendMessage.builder()
                .body(Utils.removeHtmlTags(message))
                .formattedBody(message)
                .build();
        HttpEntity<MatrixSendMessage> httpEntity = new HttpEntity<>(mxRequest, getMessageHeaders());
        ResponseEntity<MxResponse> response = restTemplate.exchange(url,
                HttpMethod.PUT,
                httpEntity,
                MxResponse.class);
        return response.getBody();
    }
    @Override
    public MxResponse sendFileMessage(String roomId, MatrixSendMessage request) {
        String url = ServletUriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/_matrix/client/v3/rooms/")
                .path(roomId)
                .path("/send/m.room.message/")
                .path(getUniqueId())
                .toUriString();
        MatrixSendMessage mxRequest = MatrixSendMessage.builder()
                .body(Utils.removeHtmlTags(request.getBody()))
                .url(request.getUrl())
                .info(request.getInfo())
                .msgType(request.getMsgType())
                .formattedBody(request.getFormattedBody())
                .body(request.getBody())
                .build();
        HttpEntity<MatrixSendMessage> httpEntity = new HttpEntity<>(mxRequest, getFileMessageHeaders());
        ResponseEntity<MxResponse> response = restTemplate.exchange(url,
                HttpMethod.PUT,
                httpEntity,
                MxResponse.class);
        return response.getBody();
    }

    private String postFile(String file) throws WrongRequestParameterException {
        if (log.isDebugEnabled()) log.debug("[MatrixApiImpl.putImage] Try to send file '{}'", file);
        if (!isConnected()) {
            login();
        }
        File f = new File(file);
        if (!f.exists()) {
            log.error("File '{}' doesn't exists", file);
            return "";
        }
        String url = ServletUriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/_matrix/media/v3/upload")
                .queryParam("filename", f.getName())
                .toUriString();

        HttpHeaders headers = getFileHeaders(f);
        HttpEntity<byte[]> requestEntity = null;
        try (InputStream in = new FileSystemResource(file).getInputStream()) {
            requestEntity = new HttpEntity<>(IOUtils.toByteArray(in), headers);
        } catch (IOException e) {
            log.error("Send file exception: ", e);
            throw new WrongRequestParameterException(String.format("Couldn't post file '%s'", file));
        }

        ResponseEntity<MxResponse> response;
        response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, MxResponse.class);

        String res = "";
        if (response.getBody() != null ) {
            res = response.getBody().getContentUri();
            if (log.isDebugEnabled()) log.debug("[MatrixApiImpl.putFile] got eventId={}", res);
        }

        return res;
    }

    private HttpHeaders getFileHeaders(File f) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Length", String.valueOf(f.length()));
        headers.set("Content-Type", Utils.getMimeType(f.getName()));
        return headers;
    }

    /**
     * @return true if (lastLoginTime - now) < (sessionLifetime / 2)
     */
    private boolean isConnected() {
        int time = sessionLifetime > 0 ? (sessionLifetime / 2) * 60 * 60 : DEFAULT_LOGIN_INTERVAL;
        Instant now = Instant.now();
        Duration duration = Duration.between(this.lastLoginTime != null ? this.lastLoginTime : now, now);
        return duration.getSeconds() > 0 & duration.getSeconds() < time;
    }

    private HttpHeaders getMessageHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private HttpHeaders getFileMessageHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private String getUniqueId() {
        //return Instant.now().toString();
        return UUID.randomUUID().toString();
    }
}

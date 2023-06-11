package das.tools.notifier.notify.service;

import das.tools.notifier.notify.entitys.request.vb.ViberMessageType;
import das.tools.notifier.notify.entitys.request.vb.ViberSendBroadcastMessageRequest;
import das.tools.notifier.notify.entitys.response.ApplicationResponse;
import das.tools.notifier.notify.entitys.response.ResponseStatus;
import das.tools.notifier.notify.entitys.response.UploadFileInfo;
import das.tools.notifier.notify.entitys.response.vb.ViberBroadcastMessageResponse;
import das.tools.notifier.notify.entitys.response.vb.ViberGetAccountInfoResponse;
import das.tools.notifier.notify.entitys.response.vb.ViberMemberResponse;
import das.tools.notifier.notify.exceptions.WrongRequestParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ViberApiImpl implements ViberApi {
    private final RestTemplate restTemplate;

    @Value("${vb.api.key}")
    private String viberToken;
    @Value("${vb.baseUrl}")
    private String baseUrl;
    private List<String> membersList;

    public ViberApiImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    private void initApi() {
        this.membersList = getMembers();
    }

    @Override
    public List<String> getMembers() {
        List<String> res = new ArrayList<>();
        String url = ServletUriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/get_account_info")
                .toUriString();
        if (log.isDebugEnabled()) log.debug("[ViberApi getMembers] url={}", url);
        HttpEntity<Void> httpEntity = new HttpEntity<>(getHeaders());
        ResponseEntity<ViberGetAccountInfoResponse> response = restTemplate.exchange(url,
                HttpMethod.POST,
                httpEntity,
                ViberGetAccountInfoResponse.class);
        ViberGetAccountInfoResponse responseBody = response.getBody();
        if (responseBody != null && responseBody.getMembers() != null) {
            for (ViberMemberResponse member : responseBody.getMembers()) {
                res.add(member.getId());
            }
        }
        this.membersList = res;
        return res;
    }

    @Override
    public ApplicationResponse sendBroadcastTextMessage(String message, String[] recipients, UploadFileInfo fileInfo) {
        ApplicationResponse res;
        try {
            String url = ServletUriComponentsBuilder.fromHttpUrl(baseUrl)
                    .path("/broadcast_message")
                    .toUriString();
            ViberSendBroadcastMessageRequest request = null;
            if (fileInfo != null) {
                ViberMessageType messageType = getFileType(fileInfo.getFileName());
                if (messageType == ViberMessageType.picture) {
                    request = ViberSendBroadcastMessageRequest.builder()
                            // 512 - maximum message's length of those type of message in Viber API
                            .text(message.length() < MAX_PICTURE_MESSAGE_LENGTH ? message : message.substring(MAX_PICTURE_MESSAGE_LENGTH))
                            .type(messageType)
                            .broadcastList(recipients)
                            .media(fileInfo.getFileUrl())
                            .size(0)
                            .build();
                } else if (messageType == ViberMessageType.video) {
                    request = ViberSendBroadcastMessageRequest.builder()
                            .type(messageType)
                            .broadcastList(recipients)
                            .media(fileInfo.getFileUrl())
                            .size(fileInfo.getFileSize())
                            .build();
                } else if (messageType == ViberMessageType.file) {
                    request = ViberSendBroadcastMessageRequest.builder()
                            .type(messageType)
                            .broadcastList(recipients)
                            .media(fileInfo.getFileUrl())
                            .size(fileInfo.getFileSize())
                            .build();
                }
            } else { // when file wasn't in request
                request = ViberSendBroadcastMessageRequest.builder()
                        .text(message)
                        .broadcastList(recipients)
                        .size(0)
                        .build();
            }
            if (log.isDebugEnabled())
                log.debug("[ViberApi sendBroadcastTextMessage] try to send request={}", Utils.linearizedString(request));
            HttpEntity<ViberSendBroadcastMessageRequest> httpEntity = new HttpEntity<>(request, getHeaders());
            ResponseEntity<ViberBroadcastMessageResponse> response = restTemplate.exchange(url,
                    HttpMethod.POST,
                    httpEntity,
                    ViberBroadcastMessageResponse.class);
            ViberBroadcastMessageResponse responseBody = response.getBody();
            if (log.isDebugEnabled())
                log.debug("[ViberApi sendBroadcastTextMessage] got responseBody={}", responseBody);
            if (responseBody != null && responseBody.getStatus() != 0) {
                res = ApplicationResponse.builder()
                        .status(ResponseStatus.ERROR)
                        .comment(responseBody.getStatusMessage())
                        .build();
            } else if (responseBody != null) {
                res = ApplicationResponse.builder()
                        .status(ResponseStatus.OK)
                        .build();
            } else {
                throw new WrongRequestParameterException("Some thing went wrong while sending Viber message");
            }
        } catch (Exception e) {
            log.error("There was exception while sending message to Viber", e);
            res = ApplicationResponse.builder()
                    .status(ResponseStatus.ERROR)
                    .comment(String.format("Exception while sending message to Viber: %s", e.getLocalizedMessage()))
                    .build();
        }
        return res;
    }

    @Override
    public void sendMessageToUser(String userId, String message) {
        sendBroadcastTextMessage(message, userId.split(""), null);
    }

    private ViberMessageType getFileType(String file) {
        if (Utils.isImageFile(file)) {
            return ViberMessageType.picture;
        } else if (Utils.isVideoFile(file)) {
            return ViberMessageType.video;
        } else {
            return ViberMessageType.file;
        }
    }

    @Override
    public List<String> getMembersList() {
        return membersList;
    }

    @Override
    public String[] getMembersListAsArray() {
        return membersList.toArray(new String[membersList.size()]);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Viber-Auth-Token", viberToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}

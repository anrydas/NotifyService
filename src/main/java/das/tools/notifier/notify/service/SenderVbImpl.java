package das.tools.notifier.notify.service;

import das.tools.notifier.notify.entitys.request.Request;
import das.tools.notifier.notify.entitys.response.ApplicationResponse;
import das.tools.notifier.notify.entitys.response.ResponseStatus;
import das.tools.notifier.notify.entitys.response.UploadFileInfo;
import das.tools.notifier.notify.exceptions.WrongRequestParameterException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Component("viber")
@Slf4j
public class SenderVbImpl implements Sender {
    private final ViberApi viberApi;
    private final RestTemplate restTemplate;
    @Value("${app.api.key.parameter}")
    private String keyParameter;
    @Value("${app.api.key}")
    private String keyValue;
    @Value("${app.baseUrl}")
    private String baseUrl;

    public SenderVbImpl(ViberApi viberApi, RestTemplate restTemplate) {
        this.viberApi = viberApi;
        this.restTemplate = restTemplate;
    }

    @Override
    public ApplicationResponse send(Request request) {
        ApplicationResponse res;
        String message = Utils.linearizedString(request.getMessage());
        UploadFileInfo fileInfo = request.getFileInfo();
        // Check is file local or not
        // if local - send it to upload service to take file's URL
        // and send message with file's URL
        //ToDo: Change the file_info to file field as in other messengers
        if (!fileInfo.getFileUrl().toLowerCase().startsWith("http")) {
            ApplicationResponse sendFileResponse;
            try {
                sendFileResponse = sendFileToUpload(fileInfo.getFileUrl());
            } catch (WrongRequestParameterException e) {
                log.error("There was exception while sending file to server", e);
                res = ApplicationResponse.builder()
                        .status(ResponseStatus.ERROR)
                        .errorMessage("Error occurred during sending local file" + fileInfo.getFileUrl())
                        .build();
                return res;
            }
            //Update File's Info with new file URL
            fileInfo.setFileUrl(sendFileResponse.getFileInfo().getFileUrl());
        }
        res = viberApi.sendBroadcastTextMessage(message, viberApi.getMembersListAsArray(), fileInfo);
        return res;
    }

    private ApplicationResponse sendFileToUpload(String file) throws WrongRequestParameterException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add(keyParameter, keyValue);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        try {
            MultipartFile multipartFile = getMultipartFile(file);
            body.add("file", multipartFile.getResource());
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            String url = ServletUriComponentsBuilder.fromHttpUrl(baseUrl)
                    .path("/api/v1")
                    .path("/upload")
                    .toUriString();
            if (log.isDebugEnabled()) log.debug("[sendFileToUpload] got url={}", url);
            ResponseEntity<ApplicationResponse> response = restTemplate
                    .postForEntity(url, requestEntity, ApplicationResponse.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Send file to upload error ", e);
            throw new WrongRequestParameterException("Send file to upload error: " + e.getLocalizedMessage(), e);
        }
    }

    private MultipartFile getMultipartFile(String file) throws IOException {
        File f = new File(file);
        String mime = Utils.getMimeType(file);
        if (log.isDebugEnabled()) log.debug("[getMultipartFile] got mime={}", mime);
        FileInputStream input = new FileInputStream(f);
        MultipartFile multipartFile = new MockMultipartFile("file",
                f.getName(), mime, IOUtils.toByteArray(input));
        if (log.isDebugEnabled()) log.debug("[getMultipartFile] got multipartFile={}", multipartFile);
        return multipartFile;
    }
}

package das.tools.notifier.notify.service;

import das.tools.notifier.notify.entitys.request.Request;
import das.tools.notifier.notify.entitys.response.ApplicationResponse;
import das.tools.notifier.notify.entitys.response.ResponseStatus;
import das.tools.notifier.notify.entitys.response.UploadFileInfo;
import das.tools.notifier.notify.exceptions.WrongRequestParameterException;
import das.tools.notifier.notify.service.upload.UploadFile;
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
    private final UploadFile uploadFile;
    @Value("${app.api.key.parameter}")
    private String keyParameter;
    @Value("${app.api.key}")
    private String keyValue;
    @Value("${app.baseUrl}")
    private String baseUrl;

    public SenderVbImpl(ViberApi viberApi, RestTemplate restTemplate, UploadFile uploadFile) {
        this.viberApi = viberApi;
        this.restTemplate = restTemplate;
        this.uploadFile = uploadFile;
    }

    @Override
    public ApplicationResponse send(Request request) {
        ApplicationResponse res;
        String message = Utils.linearizedString(request.getMessage());
        String file = request.getFile();
        if(file != null && !"".equals(file) && request.getFileInfo() != null) {
            log.error("Wrong request: must by only one field in request - file OR file_info");
            res = ApplicationResponse.builder()
                    .status(ResponseStatus.ERROR)
                    .errorMessage("Wrong request: must by only one field in request - file OR file_info")
                    .build();
            return res;
        }
        UploadFileInfo finalFileInfo = null;
        if (file != null && !"".equals(file)) {
            // local file
            ApplicationResponse sendFileResponse = null;
            try {
                sendFileResponse = sendLocalFile(file);
                finalFileInfo = sendFileResponse.getFileInfo();
            } catch (WrongRequestParameterException e) {
                return ApplicationResponse.builder()
                        .status(ResponseStatus.ERROR)
                        .errorMessage(e.getLocalizedMessage())
                        .build();
            }
        } else if (request.getFileInfo() != null) {
            // remote file
            finalFileInfo = request.getFileInfo();
        }

        res = viberApi.sendBroadcastTextMessage(message, viberApi.getMembersListAsArray(), finalFileInfo);
        return res;
    }

    private ApplicationResponse sendLocalFile(String file) throws WrongRequestParameterException {
        ApplicationResponse res = null;
        try {
            MultipartFile multipartFile = getMultipartFile(file);
            res = uploadFile.uploadFile(multipartFile).getBody();
        } catch (IOException e) {
            log.error("There was exception while sending file to server", e);
            throw new WrongRequestParameterException(String.format("Error occurred during sending local file '%s'", file));
        }
        return res;
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

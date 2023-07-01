package das.tools.notifier.notify.service.upload;

import das.tools.notifier.notify.entitys.response.ApplicationResponse;
import das.tools.notifier.notify.entitys.response.ResponseStatus;
import das.tools.notifier.notify.entitys.response.UploadFileInfo;
import das.tools.notifier.notify.service.Sender;
import das.tools.notifier.notify.service.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

@Service
@Slf4j
public class UploadFileImpl implements UploadFile {
    protected static final String CHARS = "ABCDEFJHIJKLMNOPQRSTUVWXYZabcdefjhijklmnopqrstuvwxyz";
    @Value("${app.baseUrl}")
    private String baseUrl;

    @Override
    public ResponseEntity<ApplicationResponse> uploadFile(MultipartFile file) {
        try {
            String originalFileName = file.getOriginalFilename();
            String localFilePath = Utils.getDestFilePath(getObfuscatedFileName(originalFileName));
            if (log.isDebugEnabled()) log.debug("[uploadFile] got fullLocalFilePath={}", localFilePath);
            File dest = new File(localFilePath);
            Path destFilePath = Paths.get(dest.getAbsolutePath()).getParent();
            if(!Files.exists(destFilePath)) {
                Files.createDirectories(destFilePath);
                if (log.isDebugEnabled()) log.debug("[uploadFile] created directory={}", destFilePath);
            }
            FileCopyUtils.copy(file.getInputStream(), Files.newOutputStream(dest.toPath()));
            ApplicationResponse response = getUploadFileResponse(dest, originalFileName);
            if (log.isDebugEnabled()) log.debug("[uploadFile] got response={}", Utils.linearizedString(response.toString()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Uploading file error: ", e);
            ApplicationResponse response = ApplicationResponse.builder()
                    .status(ResponseStatus.ERROR)
                    .errorMessage("Uploading file error: " + e.getLocalizedMessage())
                    .comment("Find details in log")
                    .build();
            return ResponseEntity.internalServerError()
                    .body(response);
        }
    }
    private ApplicationResponse getUploadFileResponse(File dest, String originalFileName) {
        String resUrl = ServletUriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(Sender.API_URL_PREFIX)
                .path("/")
                .path(Sender.FILES_URL_PREFIX)
                .path("/")
                .path(dest.getName())
                .toUriString();
        if (log.isDebugEnabled()) log.debug("[getUploadFileResponse] got resUrl={}", resUrl);
        UploadFileInfo fileInfo = UploadFileInfo.builder()
                .fileName(originalFileName)
                .fileUrl(resUrl)
                .fileSize(dest.length())
                .fileMime(Utils.getMimeType(originalFileName))
                .build();
        return ApplicationResponse.builder()
                .status(ResponseStatus.OK)
                .fileInfo(fileInfo)
                .build();
    }

    private String getObfuscatedFileName(String fileName) {
        String fileExt = Utils.getFileExtension(fileName);
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder sb = new StringBuilder(String.valueOf(java.time.Instant.now().getEpochSecond()));
        for (int i = 0; i < targetStringLength; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length() - 1)));
        }
        sb.append(fileExt);
        return sb.toString();
    }

}

package das.tools.notifier.notify.service.upload;

import das.tools.notifier.notify.entitys.response.ApplicationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFile {
    ResponseEntity<ApplicationResponse> uploadFile(MultipartFile file);
}

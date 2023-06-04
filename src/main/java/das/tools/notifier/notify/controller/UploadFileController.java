package das.tools.notifier.notify.controller;

import das.tools.notifier.notify.entitys.response.ApplicationResponse;
import das.tools.notifier.notify.service.upload.UploadFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class UploadFileController {
    private final UploadFile uploadFile;

    public UploadFileController(UploadFile uploadFile) {
        this.uploadFile = uploadFile;
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<ApplicationResponse> uploadFile(@RequestPart MultipartFile file){
        return uploadFile.uploadFile(file);
    }


}

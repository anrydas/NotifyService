package das.tools.notifier.notify.controller;

import das.tools.notifier.notify.service.download.GetFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("${server.api.prefix}/api/v1")
@Slf4j
public class DownloadFileController {
    private final GetFile getFile;

    public DownloadFileController(GetFile getFile) {
        this.getFile = getFile;
    }

    @GetMapping(value = "/files/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> downloadFile(@PathVariable String fileName) throws IOException {
        ResponseEntity<?> response = getFile.downloadFile(fileName);
        if (response.getBody() != null && response.getBody() instanceof InputStreamResource) {
            return ResponseEntity.ok(((InputStreamResource) response.getBody()));
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }
}

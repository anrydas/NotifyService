package das.tools.notifier.notify.service.download;

import org.springframework.http.ResponseEntity;

public interface GetFile {
    ResponseEntity<?> downloadFile(String name);
}

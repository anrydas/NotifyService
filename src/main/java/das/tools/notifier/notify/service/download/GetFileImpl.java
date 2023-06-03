package das.tools.notifier.notify.service.download;

import das.tools.notifier.notify.service.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Service
@Slf4j
public class GetFileImpl implements GetFile{
    @Override
    public ResponseEntity<?> downloadFile(String name) {
        String fullFileName = Utils.getDestFilePath(name);
        if (log.isDebugEnabled()) log.debug("[downloadFile] got fullFileName={}", fullFileName);
        InputStream in = null;
        try {
            in = new FileInputStream(fullFileName);
            MimeType mime = MimeType.valueOf(Utils.getMimeType(fullFileName));
            MediaType type = MediaType.asMediaType(mime);
            if (log.isDebugEnabled()) log.debug("[downloadFile] got type={} for mime={}", type, mime);
            return ResponseEntity.ok()
                    .contentType(type)
                    .body(new InputStreamResource(in));
        } catch (FileNotFoundException e) {
            log.error("Error downloading file: ", e);
            return ResponseEntity.internalServerError()
                    .build();
        }

    }
}

package das.tools.notifier.notify.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RemoveOldFilesService {
    public static final int DEV_INTERVAL = 10 * 1000;
    public static final String DEV_PROFILE_NAME = "dev";
    @Value("${vb.media.clear_temp}")
    private boolean isActive = false;
    @Value("${vb.media.clear_interval.days}")
    private int interval = 16;
    private final Environment environment;

    public RemoveOldFilesService(Environment environment) {
        this.environment = environment;
    }

    @Async
    //@Scheduled(initialDelay = 8 * 60 * 1000, fixedDelay = interval * 1000 * 60 * 60 * 24)
    public void scheduleOldFilesRemove() {
        if (isServiceActive()) {
            String dir = Utils.getDestFilePath("");
            if (log.isDebugEnabled()) log.debug("[scheduleOldFilesRemove]: got dir='{}'", dir);
            List<String> files = getFilesLIst(dir);
            if (log.isDebugEnabled()) log.debug("[scheduleOldFilesRemove]: got files='{}'", files);
            if (!isRunInDev()) {
                //when it's not a dev environment -remove files
                for (String file : files) {
                    try {
                        Path fileToDelete = Paths.get(file);
                        Files.delete(fileToDelete);
                    } catch (IOException e) {
                        log.error("Couldn't delete file '{}'", file);
                    }
                }
            } else {
                log.info("Files to be deleted: {}", files);
            }
        }
    }

    public long getInterval() {
        int i = getIntervalDependOnProfile();
        if (log.isDebugEnabled()) log.debug("[getInterval]: delaying '{}'", i);
        return i;
    }

    private int getIntervalDependOnProfile() {
        String[] profiles = environment.getActiveProfiles();
        int res = interval * 1000 * 60 * 60 * 24;
        if (profiles.length > 0) {
            res = DEV_PROFILE_NAME.equals(profiles[0]) ? DEV_INTERVAL : res;
        }
        return res;
    }

    private boolean isRunInDev() {
        return environment.getActiveProfiles()[0].equals(DEV_PROFILE_NAME);
    }

    private List<String> getFilesLIst(String dir) {
        List<String> res = new ArrayList<>();
        File d = new File(dir);
        File[] dirFiles = d.listFiles();
        if (dirFiles != null) {
            for (File f : dirFiles) {
                if (!f.isDirectory()
                        && Math.abs(f.lastModified() - System.currentTimeMillis()) > (long) getIntervalDependOnProfile()) {
                    res.add(f.getName());
                }
            }
        }
        res.sort(String::compareTo);
        return res;
    }

    public boolean isServiceActive() {
        return isActive && (interval > 0);
    }
}

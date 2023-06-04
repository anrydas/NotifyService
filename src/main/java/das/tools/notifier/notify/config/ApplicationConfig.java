package das.tools.notifier.notify.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@Slf4j
public class ApplicationConfig {
    final BuildProperties buildProperties;

    public ApplicationConfig(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @PostConstruct
    public void logAppVersion(){
        log.info(String.format("-=: Starting Application '%s' version %s :=-",
                buildProperties.getName(), buildProperties.getVersion()));
    }
}

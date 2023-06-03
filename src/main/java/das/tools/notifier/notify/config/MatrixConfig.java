package das.tools.notifier.notify.config;

import io.kamax.matrix.client._MatrixClient;
import io.kamax.matrix.client.regular.MatrixHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;

@Configuration
@Slf4j
public class MatrixConfig {
    @Value("${mx.baseUrl}")
    private String mxBaseUrl;

    @Bean
    public _MatrixClient getMatrixClient() {
        URL baseUrl = null;
        try {
            baseUrl = new URL(mxBaseUrl);
        } catch (MalformedURLException e) {
            log.error("Error getting URL", e);
        }
        return new MatrixHttpClient(baseUrl);
    }
}

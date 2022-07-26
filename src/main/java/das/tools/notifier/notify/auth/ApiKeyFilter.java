package das.tools.notifier.notify.auth;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@NoArgsConstructor
@Slf4j
public class ApiKeyFilter extends GenericFilterBean {
    @Value("${app.api.key}")
    private String apiKey;
    @Value("${app.api.key.parameter}")
    private String paramName;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req =(HttpServletRequest) servletRequest;
        String path = req.getRequestURI();
        if(log.isDebugEnabled()) log.debug("got path={}", path);
        if (!path.startsWith("/api/v1")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String hApiKey = req.getHeader(paramName);
        String key = (hApiKey == null || hApiKey.isEmpty()) ? "" : hApiKey;
        if(log.isDebugEnabled()) log.debug("got {}={}", paramName, key);
        if (key.equals(apiKey)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            String error = "Invalid API key";
            log.debug(error + "={}", key);
            HttpServletResponse resp = (HttpServletResponse) servletResponse;
            resp.reset();
        }
    }
}

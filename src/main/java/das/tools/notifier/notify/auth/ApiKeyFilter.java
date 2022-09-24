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
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String path = req.getRequestURI();
        if(log.isDebugEnabled()) log.debug("[doFilter]: got req={}", req.getQueryString());
        if(log.isDebugEnabled()) log.debug("[doFilter]: got path={}", path);
        if (!path.startsWith("/api/v1")) {
            if(log.isDebugEnabled()) log.debug("[doFilter]: path NOT contains '/api/v1'");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String hApiKey = req.getHeader(paramName);
        String key = (hApiKey == null || hApiKey.isEmpty()) ? "" : hApiKey;
        if(log.isDebugEnabled()) log.debug("[doFilter]: got {}={}", paramName, key);
        if (key.equals(apiKey)) {
            if(log.isDebugEnabled()) log.debug("[doFilter]: key equals");
            resp.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            if(path.endsWith("webhook")) {
                if(log.isDebugEnabled()) log.debug("[doFilter]: path contains 'webhook'");
                resp.setStatus(HttpServletResponse.SC_OK);
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            if(log.isDebugEnabled()) log.debug("[doFilter]: Invalid API key={}", key);
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}

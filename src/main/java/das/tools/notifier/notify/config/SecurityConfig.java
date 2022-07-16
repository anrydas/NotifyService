package das.tools.notifier.notify.config;

import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableWebSecurity
public class SecurityConfig /*extends AbstractPreAuthenticatedProcessingFilter */{
   // private String principalRequestHeader;

    /*public SecurityConfig(String principalRequestHeader) {
        this.principalRequestHeader = principalRequestHeader;
    }*/
    /*@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }*/

    /*@Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return null;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return null;
    }*/
}

package das.tools.notifier.notify.service;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PropertyResolver {
    private final Environment environment;

    public PropertyResolver(Environment environment) {
        this.environment = environment;
    }

    public int getIntValue(String propertyName) {
        return Integer.parseInt(environment.resolvePlaceholders("${" + propertyName + "}"));
    }
    public String getStringValue(String propertyName) {
        return environment.resolvePlaceholders("${" + propertyName + "}");
    }
}

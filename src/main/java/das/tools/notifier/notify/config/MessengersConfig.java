package das.tools.notifier.notify.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MessengersConfig {
    /*private RestTemplate restTemplate;

    public MessengersConfig(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Bean
    public Map<MessengerType, Sender> createMessengersMap(){
        Map<MessengerType, Sender> map = new HashMap<>();
        map.put(MessengerType.TELEGRAMM, new SendMessageTgImpl(restTemplate));
        map.put(MessengerType.VIBER, new SendMessageVbImpl(restTemplate));
        return map;
    }*/
}

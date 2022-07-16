package das.tools.notifier.notify.config;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramConfig {
    @Value("${tg.api.key}")
    private String tgApiKey;

    @Bean
    public TelegramBot getTelegramBot() {
        return new TelegramBot(tgApiKey);
    }
}

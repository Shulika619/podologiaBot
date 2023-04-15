package dev.shulika.podologiabot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Data
@Configuration
@EnableScheduling
@PropertySource("classpath:application.yaml")
public class BotConfig {
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String token;
    @Value("${bot.owner}")
    private Long owner;
}

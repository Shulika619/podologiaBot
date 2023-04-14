package dev.shulika.podologiabot.controller;

import dev.shulika.podologiabot.BotConst;
import dev.shulika.podologiabot.config.BotConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
@Data
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var originalMessage = update.getMessage();
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start" -> startCommandReceived(chatId, originalMessage.getChat().getFirstName());
                case "/contact" -> sendMessage(chatId, BotConst.CONTACT_TEXT);
                case "/help" -> sendMessage(chatId, BotConst.HELP_TEXT);
                default -> sendMessage(chatId, BotConst.NO_COMMAND_TEXT);
            }
        }
    }

    private void startCommandReceived(long chatId, String name) {
        String response = BotConst.HELLO_TEXT + name;
        log.info("IN TelegramBot :: startCommandReceived :: Replied to user: {}", name);
        sendMessage(chatId, response);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error :: execute message :: ", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }
}




package dev.shulika.podologiabot.controller;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import dev.shulika.podologiabot.BotConst;
import dev.shulika.podologiabot.config.BotConfig;
import dev.shulika.podologiabot.model.User;
import dev.shulika.podologiabot.repository.UserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
@Data
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final UserRepository userRepository;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var originalMessage = update.getMessage();
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start" -> startCommand(chatId, originalMessage);
                case "/contact" -> sendMessage(chatId, BotConst.CONTACT_TEXT);
                case "/help" -> sendMessage(chatId, BotConst.HELP_TEXT);
                default -> sendMessage(chatId, BotConst.NO_COMMAND_TEXT);
            }
        }
    }

    private void startCommand(long chatId, Message message) {
        String firstName = message.getChat().getFirstName();
        String response = BotConst.HELLO_TEXT + firstName + EmojiParser.parseToUnicode(" :wave:");
        log.info("IN TelegramBot :: startCommand:: Replied hello to user: {}", firstName);
        sendMessage(chatId, response);

        if(userRepository.findById(chatId).isEmpty()){
            User user = User.builder()
                    .id(chatId)
                    .firstName(firstName)
                    .lastName(message.getChat().getLastName())
                    .userName(message.getChat().getUserName())
                    .build();
            userRepository.save(user);
            log.info("IN TelegramBot :: startCommand:: New user: {} :: Saved", firstName);
        }
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error :: execute message :: ", e.getMessage());
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




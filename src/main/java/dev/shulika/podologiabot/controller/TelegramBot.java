package dev.shulika.podologiabot.controller;

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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

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
                case "/register" -> registerCommand(chatId, originalMessage);
                case "/contact" -> sendMessage(chatId, BotConst.CONTACT_TEXT);
                case "/help" -> sendMessage(chatId, BotConst.HELP_TEXT);
                default -> sendMessage(chatId, BotConst.NO_COMMAND_TEXT);
            }
        } else if (update.hasCallbackQuery()) {
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            String callBackData = update.getCallbackQuery().getData();
            switch (callBackData) {
                case "YES_BUTTON" -> sendEditMessage(chatId, "Вы выбрали YES", messageId);
                case "NO_BUTTON" -> sendEditMessage(chatId, "Вы выбрали NO", messageId);
            }
        }
    }

    private void registerCommand(long chatId, Message originalMessage) {
//        sendMessage(chatId, BotConst.REGISTER);
        log.info("IN TelegramBot :: registerCommand:: ChatId: {} :: Start", chatId);

        InlineKeyboardMarkup inlineMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var yesButton = new InlineKeyboardButton();
        yesButton.setText("YES");
        yesButton.setCallbackData("YES_BUTTON");
        var noButton = new InlineKeyboardButton();
        noButton.setText("NO");
        noButton.setCallbackData("NO_BUTTON");

        rowInLine.add(yesButton);
        rowInLine.add(noButton);
        rowsInLine.add(rowInLine);

        inlineMarkup.setKeyboard(rowsInLine);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(BotConst.REGISTER);

        message.setReplyMarkup(inlineMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error :: execute message :: ", e.getMessage());
        }
        log.info("IN TelegramBot :: registerCommand:: ChatId: {} :: SELECT NOW", chatId);


    }

    private void startCommand(long chatId, Message message) {
        String firstName = message.getChat().getFirstName();
        String response = BotConst.HELLO_TEXT + firstName + EmojiParser.parseToUnicode(" :wave:");
        log.info("IN TelegramBot :: startCommand:: Replied hello to user: {}", firstName);
        sendMessage(chatId, response);

        if (userRepository.findById(chatId).isEmpty()) {
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
            log.info("IN TelegramBot :: sendMessage:: chatId:{} :: Text Sent", chatId);
        } catch (TelegramApiException e) {
            log.error("Error :: execute message :: ", e.getMessage());
        }
    }

    private void sendEditMessage(long chatId, String textToSend, long messageId) {
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .text(textToSend)
                .messageId((int) messageId)
                .build();
        try {
            execute(message);
            log.info("IN TelegramBot :: sendEditMessage:: ChatId: {} :: Text: {}", chatId, textToSend);
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




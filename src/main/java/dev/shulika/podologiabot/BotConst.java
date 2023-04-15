package dev.shulika.podologiabot;

import com.vdurmont.emoji.EmojiParser;

public class BotConst {

/*
 :::::  List Commands:::::
start - Начало
register - Запись онлайн
contact - Контакты
help - Помощь
/send - Отправить рассылку (*только Админ)
*/

    public static final String HELLO_TEXT = "Привет, ";
    public static final String REGISTER = EmojiParser.parseToUnicode("Онлайн запись :writing_hand:");
    public static final String YES_TXT = "YES";
    public static final String NO_TXT = "NO";
    public static final String YES_BUTTON = "YES_BUTTON";
    public static final String NO_BUTTON = "NO_BUTTON";
    public static final String NO_COMMAND_TEXT = "Извините, такой команды нет!";
    public static final String HELP_TEXT = EmojiParser.parseToUnicode("""
                Список доступных команд:\n
                :arrow_forward: /start - Начало работы\n
                :writing_hand: /register - Запись онлайн\n
                :telephone_receiver: /contact - Контакты\n
                :information_source: /help - Помощь\n
                :speech_balloon: Дополнительно: \n
                ...
            """);
    public static final String CONTACT_TEXT = EmojiParser.parseToUnicode(""" 
                КОНТАКТЫ: \n
                :telephone_receiver: Тел: 111-111-111\t 222-222-222\n
                :round_pushpin: Адрес: ул. Пупкина 9\n
                :speech_balloon: Телеграм: @PodologiaLnrBot\n
            """);
    public static final String SEND_TEXT = EmojiParser.parseToUnicode("Введите текст для рассылки всем пользователям :writing_hand: ");
}

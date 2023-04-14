package dev.shulika.podologiabot;

public class BotConst {

// List Commands:
//  start - Начало
//  contact - Контакты
//  help - Помощь

    public static final String HELLO_TEXT = "Привет, ";
    public static final String NO_COMMAND_TEXT = "Извините, такой команды нет!";
    public static final String HELP_TEXT = """
                Список доступных команд:\n
                /start - Начало работы\n
                /contact - Контакты\n
                /help - Помощь и доп. информация\n
                Дополнительно: \n
                ...
            """;
    public static final String CONTACT_TEXT = """ 
                КОНТАКТЫ:\n
                тел: 111-111-111\t 222-222-222\n
                адрес: ул. Пупкина 9\n
                Телеграм: @PodologiaLnrBot\n
            """;
}

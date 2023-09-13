package ru.example.myjavatelebot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyTeleBot extends TelegramLongPollingBot {

    @Value("${telegram.botName}")
    private String botName;

    public MyTeleBot(@Value("${telegram.token}") String botToken) {
        super(botToken);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            final Message message = update.getMessage();
            final Long chatId = message.getChatId();
            final String responseText;
            if (message.hasText()) {
                final String messageText = message.getText();
                switch (messageText) {
                    case "/start":
                        responseText = "Добро пожаловать!";
                        break;
                    case "Кнопка 1":
                        responseText = "Вы нажали кнопку №1";
                        break;
                    case "Кнопка 2":
                        responseText = "Вы нажали кнопку №2";
                        break;
                    case "Кнопка 3":
                        responseText = "Вы нажали кнопку №3";
                        break;
                    case "Кнопка 4":
                        responseText = "Вы нажали кнопку №4";
                        break;
                    default:
                        responseText = String.format("Вы написали: *%s*", messageText);
                        break;
                }
            } else
                responseText = "Я понимаю только текст";
            sendNotification(chatId, responseText);
        }
    }

    private void sendNotification(Long chatId, String responseText) {
        SendMessage responseMessage = new SendMessage(chatId.toString(), responseText);
        responseMessage.enableMarkdown(true);
        responseMessage.setReplyMarkup(getReplyMarkup());
        try {
            execute(responseMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private ReplyKeyboardMarkup getReplyMarkup() {
        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(new KeyboardButton("Кнопка 1"));
        keyboardFirstRow.add(new KeyboardButton("Кнопка 2"));

        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardSecondRow.add(new KeyboardButton("Кнопка 3"));
        keyboardSecondRow.add(new KeyboardButton("Кнопка 4"));

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        // Устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

}

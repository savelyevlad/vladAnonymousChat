package botStuff;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SendSomethingCreator {

    private static Bot bot;

    public static void setBot(Bot bot) {
        SendSomethingCreator.bot = bot;
    }

    static void messageToSendSomething(Message message, long chatId) {
        System.out.println(message.hasSticker());
        if(message.hasText()) {
            sendMessage(chatId, message.getText());
        }
        else if(message.hasSticker()) {
            // TODO: send sticker
            sendSticker(chatId, message.getSticker());
        }
    }

    private static void sendSticker(long chatId, Sticker sticker) {
        // TODO: send sticker
    }

    private static void sendMessage(long chatId, String messageText) {
        try {
            bot.execute(new SendMessage()
                    .setChatId(chatId)
                    .setText(messageText));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

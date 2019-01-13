package botStuff;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
            sendSticker(chatId, message.getSticker());
        }
        else if(message.hasPhoto()) {
            sendPhoto(chatId, message);
        }
    }

    private static void sendPhoto(long chatId, Message message) {
        String filePath = "photos\\" + message.getPhoto().get(0).getFileId();
        downloadFileViaFileId("photos", message.getPhoto().get(0).getFileId());
        SendPhoto sendPhoto = new SendPhoto()
                .setPhoto(new java.io.File(filePath))
                .setCaption(message.getCaption())
                .setChatId(chatId);
        try {
            bot.execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        deleteFile(filePath);
    }

    private static void sendSticker(long chatId, Sticker sticker) {
        // TODO: send sticker
        String filePath = "stickers\\" + sticker.getFileId();
        if (!fileExists(filePath)) {
            downloadFileViaFileId("stickers", sticker.getFileId());
        }
        SendSticker sendSticker = new SendSticker()
                .setSticker(new java.io.File(filePath))
                .setChatId(chatId);
        try {
            bot.execute(sendSticker);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private static void deleteFile(String filePath) {
        java.io.File file = new java.io.File(filePath);
        if(!file.delete()) {
            try {
                throw new IOException("Photo was not deleted");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean fileExists(String filePath) {
        java.io.File file = new java.io.File(filePath);
        return file.exists();
    }

    private static void downloadFileViaFileId(String folder, String fileId) {
        GetFile getFile = new GetFile().setFileId(fileId);
        byte[] output = null;
        try {
            // Getting url
            File file = bot.execute(getFile);
            URL url = new URL(file.getFileUrl(bot.getBotToken()));
            // Downloading via url
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            output = IOUtils.toByteArray(inputStream);
            inputStream.close();
            httpURLConnection.disconnect();
        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();
        }
        writeBytesToFile(folder, fileId, output);
    }

    private static void writeBytesToFile(String folder, String fileName, byte[] output) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(folder + "\\" + fileName);
            fileOutputStream.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

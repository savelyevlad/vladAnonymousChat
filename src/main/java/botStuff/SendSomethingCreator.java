package botStuff;

import org.apache.commons.io.IOUtils;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.api.objects.games.Animation;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class SendSomethingCreator {

    private static Bot bot;

    static void setBot(Bot bot) {
        SendSomethingCreator.bot = bot;
    }

    static void messageToSendSomething(Message message, long chatId) {
        if(message.hasText()) {
            sendMessage(chatId, message.getText());
        }
        else if(message.hasSticker()) {
            sendSticker(chatId, message.getSticker());
        }
        else if(message.hasPhoto()) {
            sendPhoto(chatId, message);
        }
        else if(message.getVoice() != null) {
            sendVoice(chatId, message.getVoice());
        }
        else if(message.hasAnimation()) {
            // TODO: sendAnimation
            sendAnimation(chatId, message);
        }
        else if(message.hasDocument()) {
            sendDocument(chatId, message);
        }
        else if(message.getAudio() != null) {
            sendAudio(chatId, message);
        }
    }

    private static void sendAnimation(long chatId, Message message) {
        // TODO: sendAnimation
        String filePath = "animations\\" + message.getAnimation().getFileId();
        downloadFileViaFileId("animations", message.getAnimation().getFileId());
        SendAnimation sendAnimation = new SendAnimation()
                .setAnimation(new java.io.File(filePath))
                .setChatId(chatId)
                .setCaption(message.getCaption());
        try {
            bot.execute(sendAnimation);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        deleteFile(filePath);
    }

    private static void sendAudio(long chatId, Message message) {
        String filePath = "audio\\" + message.getAudio().getFileId();
        downloadFileViaFileId("audio", message.getAudio().getFileId());
        SendAudio sendAudio = new SendAudio()
                .setAudio(new java.io.File(filePath))
                .setCaption(message.getCaption())
                .setChatId(chatId);
        try {
            bot.execute(sendAudio);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        deleteFile(filePath);
    }

    private static void sendVoice(long chatId, Voice voice) {
        String filePath = "voices\\" + voice.getFileId();
        downloadFileViaFileId("voices", voice.getFileId());
        SendVoice sendVoice = new SendVoice()
                .setVoice(new java.io.File(filePath))
                .setChatId(chatId);
        try {
            bot.execute(sendVoice);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        deleteFile(filePath);
    }

    private static void sendPhoto(long chatId, Message message) {

        int i = 0, maxWidth = 0, counter = 0;

        for(PhotoSize photoSize : message.getPhoto()) {
            if(photoSize.getWidth() > maxWidth) {
                maxWidth = photoSize.getWidth();
                i = counter;
            }
            ++counter;
        }

        String filePath = "photos\\" + message.getPhoto().get(i).getFileId();
        downloadFileViaFileId("photos", message.getPhoto().get(i).getFileId());
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

    private static void sendDocument(long chatId, Message message) {
        String filePath = "docs\\" + message.getDocument().getFileName();
        downloadFileViaFileId("docs", message.getDocument().getFileId(), message.getDocument().getFileName());
        SendDocument sendDocument = new SendDocument()
                .setDocument(new java.io.File(filePath))
                .setCaption(message.getCaption())
                .setChatId(chatId);
        try {
            bot.execute(sendDocument);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        deleteFile(filePath);
    }

    private static void deleteFile(String filePath) {
        java.io.File file = new java.io.File(filePath);
        if(!file.delete()) {
            try {
                throw new IOException("Не удалось удалить файл");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean fileExists(String filePath) {
        java.io.File file = new java.io.File(filePath);
        return file.exists();
    }

    private static void downloadFileViaFileId(String folder, String fileId, String fileName) {
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
        writeBytesToFile(folder, fileName, output);
    }

    private static void downloadFileViaFileId(String folder, String fileId) {
        downloadFileViaFileId(folder, fileId, fileId);
    }

    private static void writeBytesToFile(String folder, String fileName, byte[] output) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(folder + "\\" + fileName);
            fileOutputStream.write(output);
            fileOutputStream.close();
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

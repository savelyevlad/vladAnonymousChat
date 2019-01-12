package botStuff;

import static justStuff.FileReader.fileToString;

class BotInfo {

    static String getBotUsername() {
        return fileToString("configure/botUsername");
    }

    static String getBotToken() {
        return fileToString("configure/botToken");
    }

    static String getOnStart() {
        return fileToString("configure/onStartCommand");
    }
}

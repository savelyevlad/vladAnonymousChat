package botStuff;

import databaseStuff.UserAndChatId;
import justStuff.Pair;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class Bot extends TelegramLongPollingBot {

    private User searchingUser;
    private HashMap<User, User> connectedUsers = new HashMap<>();

    Bot() {

        System.out.println("Bot was started!");
        SendSomethingCreator.setBot(this);

        // TODO: addButtons
    }

    public synchronized void onUpdateReceived(Update update) {

        // updates chatId for user in database
        if(update.hasMessage()) {
            UserAndChatId.insert(new Pair<>(update.getMessage().getFrom(), update.getMessage().getChatId()));

            System.out.println("Message from " + update.getMessage().getFrom().getId() +
                    (update.getMessage().hasText() ? ": " + update.getMessage().getText() : ""));
        }

        if(update.hasMessage()) {
            if(update.getMessage().hasText()) {
                switch (update.getMessage().getText()) {
                    case "/start":
                        botStart(update.getMessage().getChatId());
                        break;
                    case "/new":
                        commandNew(update);
                        break;
                    case "/stop":
                        disconnectUser(update.getMessage().getFrom());
                        break;
                    default:
                        defaultMessage(update);
                        break;
                }
            }
            else {
                defaultMessage(update);
            }
        }
    }

    private void defaultMessage(Update update) {
        if (connectedUsers.containsKey(update.getMessage().getFrom())) {
            sendMessage(connectedUsers.get(update.getMessage().getFrom()), update.getMessage());
        }
        else {
            sendMessage(update.getMessage().getFrom(), "You are not in dialog");
        }
    }

    // disconnects users (user1 wrote "/stop"_
    private void disconnectUser(User user1) {
        if(connectedUsers.containsKey(user1)) {
            User user2 = connectedUsers.get(user1);
            connectedUsers.remove(user2);
            connectedUsers.remove(user1);
            sendMessage(user2, "Your interlocutor stopped the chat");
            sendMessage(user1, "You stopped the chat");
        }
        else {
            if(searchingUser.equals(user1)) {
                sendMessage(user1, "You stopped searching");
                searchingUser = null;
            }
            else {
                sendMessage(user1, "You are not in dialog!");
            }
        }
    }

    // connects 2 users
    private void connectUsers(User user1, User user2) {
        connectedUsers.put(user1, user2);
        connectedUsers.put(user2, user1);
        sendMessage(user1, "Interlocutor was found. You can speak right now!");
        sendMessage(user2, "Interlocutor was found. You can speak right now!");
    }

    // if there was "/new" command
    private synchronized void commandNew(Update update) {
        if(connectedUsers.containsKey(update.getMessage().getFrom())) {
            disconnectUser(update.getMessage().getFrom());
        }
        if(searchingUser == null) {
            sendMessage(update.getMessage().getFrom(), "We are looking for an interlocutor. You can stop search using command \"/stop\"");
            searchingUser = update.getMessage().getFrom();
        }
        else if(searchingUser.equals(update.getMessage().getFrom())) {
            sendMessage(update.getMessage().getChatId(), "Wait plz");
        }
        else {
            connectUsers(searchingUser, update.getMessage().getFrom());
            searchingUser = null;
        }
    }

    // reads "onStartCommand" and output it
    private void botStart(long chatId) {
        sendMessage(chatId, BotInfo.getOnStart());
    }

    private void sendMessage(User user, String messageText) {
        System.out.println(UserAndChatId.getChatId(user));
        sendMessage(UserAndChatId.getChatId(user), messageText);
    }

    private void sendMessage(User user, Message message) {
        sendMessage(UserAndChatId.getChatId(user), message);
    }

    private void sendMessage(long chatId, Message message) {
        SendSomethingCreator.messageToSendSomething(message, chatId);
    }

    private void sendMessage(long chatId, String messageText) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(messageText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return BotInfo.getBotUsername();
    }

    public String getBotToken() {
        return BotInfo.getBotToken();
    }
}

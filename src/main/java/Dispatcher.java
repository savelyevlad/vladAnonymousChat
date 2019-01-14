import botStuff.BotInitializer;
import databaseStuff.DatabaseConnector;
import justStuff.AdminThread;

public class Dispatcher {

    public static void main(String[] args) throws ClassNotFoundException {

        // connects to mysql database
        new DatabaseConnector().connect();
        // creates and runs bot
        BotInitializer botInitializer = new BotInitializer();
        botInitializer.start();
        // Console control
        new Thread(new AdminThread(botInitializer.getBot())).start();
    }
}

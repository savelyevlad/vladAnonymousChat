import botStuff.BotInitializer;
import databaseStuff.DatabaseConnector;

public class Dispatcher {

    public static void main(String[] args) throws ClassNotFoundException {

        // connects to mysql database
        new DatabaseConnector().connect();
        // creates and runs bot
        new BotInitializer().start();
    }
}

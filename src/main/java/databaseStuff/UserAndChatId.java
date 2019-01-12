package databaseStuff;

import justStuff.Pair;
import org.telegram.telegrambots.meta.api.objects.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserAndChatId {

    private static Statement statement;
    private static Connection connection;

    static void run(Statement statement, Connection connection) {

        UserAndChatId.connection = connection;
        UserAndChatId.statement = statement;

        try {
            statement.executeUpdate("create table if not exists userAndChatId(id int not null, chatid int not null, primary key (id));");
            System.out.println("Database successfully created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insert(Pair<User, Long> userAndCharId) {
        try {
            statement.executeUpdate("delete from userAndChatId where id=" +
                                        userAndCharId.getFirst().getId() + ";");
            statement.executeUpdate("insert into userAndChatId values (" +
                                        userAndCharId.getFirst().getId() + ", " + userAndCharId.getSecond() + ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getChatId(User user) {
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("select chatid from userAndChatId where id = " + user.getId() + ";");
            resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            return resultSet.getInt("chatid");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }
}

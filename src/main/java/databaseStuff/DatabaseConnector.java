package databaseStuff;

import java.sql.*;

public class DatabaseConnector {

    private Statement statement;
    private Connection connection;

    public Statement getStatement() {
        return statement;
    }

    public Connection getConnection() {
        return connection;
    }

    public void connect() throws ClassNotFoundException {

        Class.forName("com.mysql.jdbc.Driver");

        try {
            String connectionURL = "jdbc:mysql://localhost:3306/vladanonymouschatbase";
            String username = "root";
            String password = "keklelkek";
            connection = DriverManager.getConnection(connectionURL, username, password);
            statement = connection.createStatement();

            UserAndChatId.run(statement, connection);

//            statement.executeUpdate("insert into temp (name) values ('kek')");
//            statement.executeUpdate("insert into temp set name = 'lel'");
//            ResultSet resultSet = statement.executeQuery("select * from temp");
//            while (resultSet.next()) {
//                System.out.println(resultSet.getInt("id"));
//                System.out.println(resultSet.getString("name"));
//                System.out.println("-----------------");
//            }
//
//            ResultSet resultSet1 = statement.executeQuery("select name from temp where id = 1");
//            resultSet1.next();
//            System.out.println(resultSet1.getString("name"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

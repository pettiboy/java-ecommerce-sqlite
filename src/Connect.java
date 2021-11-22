package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connect {
    public static ResultSet runQuery(String query) throws SQLException {
        String url = "jdbc:sqlite://db.sqlite3";
        Connection sqlConnection = DriverManager.getConnection(url);

        Statement statement = sqlConnection.createStatement();
        return statement.executeQuery(query);

    }
}

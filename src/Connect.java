package src;

// path
import java.nio.file.Path;
import java.nio.file.Paths;

// SQL
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connect {
    static Connection sqlConnection;

    Connect() throws SQLException {
        Path currentRelativePath = Paths.get("");
        String absolutePath = currentRelativePath.toAbsolutePath().toString();

        String url = "jdbc:sqlite://" + absolutePath + "/main.db";
        Connect.sqlConnection = DriverManager.getConnection(url);
    }

    public static ResultSet runQuery(String query) {
        if (sqlConnection != null) {
            Statement statement;
            try {
                statement = sqlConnection.createStatement();

                if (query.split(" ")[0].equals("SELECT")) {
                    return statement.executeQuery(query);
                } else {
                    statement.executeUpdate(query);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else  {
           Print.print("Could not execute query as connection was not established", Print.RED);
        }
        return null;
    }
}

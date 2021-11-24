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

    // connect with database on instantiation
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

    public static int getPrevRowId() { 
        Statement statement;
        try {
            statement = sqlConnection.createStatement();
            ResultSet rowId = statement.executeQuery("select last_insert_rowid();");
            if (rowId.next()) {
                return Integer.parseInt(rowId.getString(1));
            }
            // return rowId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

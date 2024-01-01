package dacd.gil.view.databaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnector {

    public static Connection connect(String path) throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + path);
    }
}

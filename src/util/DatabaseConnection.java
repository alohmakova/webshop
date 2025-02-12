package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection class handles the connection to the database.
 * This class can be reused across different repository classes like CustomerRepository, ProductsRepository, OrderRepository, etc
 * If it is needed to change URL, you need to do it only in one place - here
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:resources/webbutiken.db";//database was changed

    /**
     * Gets a connection to the database.
     *Skapar en ny anslutning till databasen varje gång metoden anropas
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
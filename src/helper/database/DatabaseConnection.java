package helper.database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * This class handles the connection to the MySQL database.
 */
public abstract class DatabaseConnection {

    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String url = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static final String password = "Passw0rd!"; // Password
    private static Connection connection;  // Connection Interface

    /**
     * Opens the connection to the MySQL database.
     */
    public static void openConnection() {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(url, userName, password); // Reference Connection object
            System.out.println("[MySQL] Successfully opened connection to database");
        } catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Returns current instance of Connection.
     *
     * @return current Connection instance
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Closes the connection to the MySQL database.
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("[MySQL] Successfully closed connection to database");
        } catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}

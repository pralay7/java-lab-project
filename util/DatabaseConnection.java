package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton utility class for managing the SQLite database connection.
 */
public class DatabaseConnection {
    private static final String DATABASE_URL = "jdbc:sqlite:hospital.db";
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(DATABASE_URL);
            initializeDatabase();
        } catch (SQLException e) {
            System.err.println("Unable to connect to the database: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void initializeDatabase() {
        String createPatientTableSQL = "CREATE TABLE IF NOT EXISTS patients ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "age INTEGER NOT NULL,"
                + "gender TEXT NOT NULL,"
                + "contactNumber TEXT NOT NULL,"
                + "medicalHistory TEXT"
                + ");";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createPatientTableSQL);
        } catch (SQLException e) {
            System.err.println("Failed to initialize database tables: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

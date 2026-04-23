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
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DATABASE_URL);
            initializeDatabase();
        } catch (SQLException e) {
            System.err.println("Unable to connect to the database: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found: " + e.getMessage());
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

        String createDoctorTableSQL = "CREATE TABLE IF NOT EXISTS doctors ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "degree TEXT NOT NULL,"
                + "specialty TEXT NOT NULL,"
                + "contactNumber TEXT NOT NULL"
                + ");";

        String createPatientDoctorTableSQL = "CREATE TABLE IF NOT EXISTS patient_doctors ("
                + "patientId INTEGER NOT NULL,"
                + "doctorId INTEGER NOT NULL,"
                + "PRIMARY KEY (patientId, doctorId),"
                + "FOREIGN KEY (patientId) REFERENCES patients(id),"
                + "FOREIGN KEY (doctorId) REFERENCES doctors(id)"
                + ");";

        String insertDefaultDoctorsSQL = "INSERT OR IGNORE INTO doctors (id, name, degree, specialty, contactNumber) VALUES "
                + "(1, 'Dr. Smith', 'MD', 'General Practitioner', '555-0001'), "
                + "(2, 'Dr. Johnson', 'MD', 'Cardiologist', '555-0002'), "
                + "(3, 'Dr. Williams', 'MD', 'Neurologist', '555-0003'), "
                + "(4, 'Dr. Brown', 'MD', 'Dermatologist', '555-0004'), "
                + "(5, 'Dr. Jones', 'MD', 'Orthopedist', '555-0005');";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createPatientTableSQL);
            statement.execute(createDoctorTableSQL);
            statement.execute(createPatientDoctorTableSQL);
            statement.execute(insertDefaultDoctorsSQL);
        } catch (SQLException e) {
            System.err.println("Failed to initialize database tables: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

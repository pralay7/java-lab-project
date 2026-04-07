# Patient Management System

A Core Java console application for managing patient records using SQLite and JDBC.

## Features

- Add a new patient with name, age, gender, contact number, and medical history
- View all patients stored in the database
- Search for a patient by ID
- Update an existing patient's details
- Delete a patient record by ID
- Automatic SQLite database creation (`hospital.db`) and table initialization on startup
- DAO design pattern for separation of persistence and business logic
- Console-based interactive menu using `java.util.Scanner`
- Robust handling for invalid user input and JDBC exceptions

## Project Structure

- `util/DatabaseConnection.java` - Singleton class to manage the SQLite JDBC connection and initialize the `patients` table
- `model/Patient.java` - Patient entity class with fields, constructors, getters, setters, and `toString()`
- `dao/PatientDAO.java` - Interface defining CRUD methods for patient data
- `dao/PatientDAOImpl.java` - JDBC implementation of the DAO interface
- `service/PatientService.java` - Business layer for validation and service operations
- `Main.java` - Application entry point with the interactive console menu

## Requirements

- JDK 17 or newer
- SQLite JDBC driver JAR (for example: `sqlite-jdbc-3.42.0.0.jar`)

## Compile and Run

1. Place the SQLite JDBC JAR in the project root.
2. Compile the Java classes:
   ```cmd
   cd /d "c:\Users\rmrid\OneDrive\Desktop\javaLAbproject"
   javac util\DatabaseConnection.java model\Patient.java dao\PatientDAO.java dao\PatientDAOImpl.java service\PatientService.java Main.java
   ```
3. Run the application:
   ```cmd
   java -cp ".;sqlite-jdbc-3.42.0.0.jar" Main
   ```

## Notes

- The application creates `hospital.db` automatically when it first runs.
- Data is persisted locally in the SQLite database.
- Use the menu options to manage patient records from the console.

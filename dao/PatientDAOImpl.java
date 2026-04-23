package dao;

import model.Patient;
import model.Doctor;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of the PatientDAO interface.
 */
public class PatientDAOImpl implements PatientDAO {
    private final Connection connection;

    public PatientDAOImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public boolean addPatient(Patient patient) {
        String sql = "INSERT INTO patients(name, age, gender, contactNumber, medicalHistory) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, patient.getName());
            preparedStatement.setInt(2, patient.getAge());
            preparedStatement.setString(3, patient.getGender());
            preparedStatement.setString(4, patient.getContactNumber());
            preparedStatement.setString(5, patient.getMedicalHistory());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding patient: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Patient getPatientById(int id) {
        String sql = "SELECT * FROM patients WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Patient patient = mapResultSetToPatient(resultSet);
                loadDoctors(patient);
                return patient;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching patient by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY id";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Patient patient = mapResultSetToPatient(resultSet);
                loadDoctors(patient);
                patients.add(patient);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all patients: " + e.getMessage());
        }
        return patients;
    }

    @Override
    public boolean updatePatient(Patient patient) {
        String sql = "UPDATE patients SET name = ?, age = ?, gender = ?, contactNumber = ?, medicalHistory = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, patient.getName());
            preparedStatement.setInt(2, patient.getAge());
            preparedStatement.setString(3, patient.getGender());
            preparedStatement.setString(4, patient.getContactNumber());
            preparedStatement.setString(5, patient.getMedicalHistory());
            preparedStatement.setInt(6, patient.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating patient: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deletePatient(int id) {
        String sql = "DELETE FROM patients WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting patient: " + e.getMessage());
            return false;
        }
    }

    private Patient mapResultSetToPatient(ResultSet resultSet) throws SQLException {
        return new Patient(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getInt("age"),
                resultSet.getString("gender"),
                resultSet.getString("contactNumber"),
                resultSet.getString("medicalHistory")
        );
    }

    private void loadDoctors(Patient patient) {
        String sql = "SELECT d.* FROM doctors d " +
                     "JOIN patient_doctors pd ON d.id = pd.doctorId " +
                     "WHERE pd.patientId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, patient.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Doctor doctor = new Doctor(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("degree"),
                        resultSet.getString("specialty"),
                        resultSet.getString("contactNumber")
                );
                patient.addDoctor(doctor);
            }
        } catch (SQLException e) {
            System.err.println("Error loading doctors for patient: " + e.getMessage());
        }
    }

    public boolean assignDoctorToPatient(int patientId, int doctorId) {
        String sql = "INSERT OR IGNORE INTO patient_doctors(patientId, doctorId) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, patientId);
            preparedStatement.setInt(2, doctorId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error assigning doctor to patient: " + e.getMessage());
            return false;
        }
    }

    public boolean removeDoctorFromPatient(int patientId, int doctorId) {
        String sql = "DELETE FROM patient_doctors WHERE patientId = ? AND doctorId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, patientId);
            preparedStatement.setInt(2, doctorId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error removing doctor from patient: " + e.getMessage());
            return false;
        }
    }
}

package dao;

import model.Doctor;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of the DoctorDAO interface.
 */
public class DoctorDAOImpl implements DoctorDAO {
    private final Connection connection;

    public DoctorDAOImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public boolean addDoctor(Doctor doctor) {
        String sql = "INSERT INTO doctors(name, degree, specialty, contactNumber) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, doctor.getName());
            preparedStatement.setString(2, doctor.getDegree());
            preparedStatement.setString(3, doctor.getSpecialty());
            preparedStatement.setString(4, doctor.getContactNumber());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding doctor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Doctor getDoctorById(int id) {
        String sql = "SELECT * FROM doctors WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Doctor doctor = mapResultSetToDoctor(resultSet);
                loadPatientIds(doctor);
                return doctor;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching doctor by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors ORDER BY id";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Doctor doctor = mapResultSetToDoctor(resultSet);
                loadPatientIds(doctor);
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all doctors: " + e.getMessage());
        }
        return doctors;
    }

    @Override
    public List<Doctor> getDoctorsBySpecialty(String specialty) {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors WHERE specialty = ? ORDER BY id";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, specialty);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Doctor doctor = mapResultSetToDoctor(resultSet);
                loadPatientIds(doctor);
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching doctors by specialty: " + e.getMessage());
        }
        return doctors;
    }

    @Override
    public boolean updateDoctor(Doctor doctor) {
        String sql = "UPDATE doctors SET name = ?, degree = ?, specialty = ?, contactNumber = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, doctor.getName());
            preparedStatement.setString(2, doctor.getDegree());
            preparedStatement.setString(3, doctor.getSpecialty());
            preparedStatement.setString(4, doctor.getContactNumber());
            preparedStatement.setInt(5, doctor.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating doctor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteDoctor(int id) {
        String sql = "DELETE FROM doctors WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting doctor: " + e.getMessage());
            return false;
        }
    }

    private Doctor mapResultSetToDoctor(ResultSet resultSet) throws SQLException {
        return new Doctor(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("degree"),
                resultSet.getString("specialty"),
                resultSet.getString("contactNumber")
        );
    }

    private void loadPatientIds(Doctor doctor) {
        String sql = "SELECT patientId FROM patient_doctors WHERE doctorId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, doctor.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                doctor.addPatientId(resultSet.getInt("patientId"));
            }
        } catch (SQLException e) {
            System.err.println("Error loading patient IDs for doctor: " + e.getMessage());
        }
    }
}

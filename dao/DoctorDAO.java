package dao;

import model.Doctor;
import java.util.List;

/**
 * Data Access Object interface for Doctor operations.
 */
public interface DoctorDAO {
    boolean addDoctor(Doctor doctor);
    Doctor getDoctorById(int id);
    List<Doctor> getAllDoctors();
    List<Doctor> getDoctorsBySpecialty(String specialty);
    boolean updateDoctor(Doctor doctor);
    boolean deleteDoctor(int id);
}

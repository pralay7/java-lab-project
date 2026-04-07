package dao;

import model.Patient;
import java.util.List;

/**
 * Interface defining CRUD operations for Patient entities.
 */
public interface PatientDAO {
    boolean addPatient(Patient patient);
    Patient getPatientById(int id);
    List<Patient> getAllPatients();
    boolean updatePatient(Patient patient);
    boolean deletePatient(int id);
}

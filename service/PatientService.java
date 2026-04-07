package service;

import dao.PatientDAO;
import dao.PatientDAOImpl;
import model.Patient;

import java.util.List;

/**
 * Business service layer for patient operations and validation.
 */
public class PatientService {
    private final PatientDAO patientDAO;

    public PatientService() {
        this.patientDAO = new PatientDAOImpl();
    }

    public boolean addPatient(Patient patient) {
        if (patient == null) {
            return false;
        }
        if (!isValidPatient(patient)) {
            return false;
        }
        return patientDAO.addPatient(patient);
    }

    public Patient getPatientById(int id) {
        if (id <= 0) {
            return null;
        }
        return patientDAO.getPatientById(id);
    }

    public List<Patient> getAllPatients() {
        return patientDAO.getAllPatients();
    }

    public boolean updatePatient(Patient patient) {
        if (patient == null || patient.getId() == null || patient.getId() <= 0) {
            return false;
        }
        if (!isValidPatient(patient)) {
            return false;
        }
        return patientDAO.updatePatient(patient);
    }

    public boolean deletePatient(int id) {
        if (id <= 0) {
            return false;
        }
        return patientDAO.deletePatient(id);
    }

    private boolean isValidPatient(Patient patient) {
        if (patient.getName() == null || patient.getName().trim().isEmpty()) {
            System.err.println("Patient name is required.");
            return false;
        }
        if (patient.getAge() == null || patient.getAge() <= 0) {
            System.err.println("Patient age must be a positive integer.");
            return false;
        }
        if (patient.getGender() == null || patient.getGender().trim().isEmpty()) {
            System.err.println("Patient gender is required.");
            return false;
        }
        if (patient.getContactNumber() == null || patient.getContactNumber().trim().isEmpty()) {
            System.err.println("Patient contact number is required.");
            return false;
        }
        return true;
    }
}

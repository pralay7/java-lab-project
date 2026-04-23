package service;

import dao.PatientDAO;
import dao.PatientDAOImpl;
import model.Patient;
import util.SymptomUtils;

import java.util.List;
import java.util.Scanner;

/**
 * Business service layer for patient operations and validation.
 */
public class PatientService {
    private final PatientDAO patientDAO;
    private final DoctorAssignmentService doctorAssignmentService;

    public PatientService() {
        this.patientDAO = new PatientDAOImpl();
        this.doctorAssignmentService = new DoctorAssignmentService();
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

    public String displaySymptomMenu(Scanner scanner) {
        SymptomUtils.displaySymptomMenu();
        System.out.print("Enter your choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());

            if (SymptomUtils.isSkipOption(choice)) {
                return null;
            }

            String symptom = SymptomUtils.getSymptomByIndex(choice);
            if (symptom != null) {
                return symptom;
            } else {
                System.out.println("Invalid selection. Please try again.");
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return null;
        }
    }

    public boolean updateMedicalHistoryWithSymptoms(int patientId, String newSymptom, Scanner scanner) {
        Patient patient = getPatientById(patientId);
        if (patient == null) {
            System.out.println("Patient not found.");
            return false;
        }

        String currentHistory = patient.getMedicalHistory();
        String updatedHistory;

        if (currentHistory == null || currentHistory.trim().isEmpty()) {
            updatedHistory = newSymptom;
        } else {
            updatedHistory = currentHistory + ", " + newSymptom;
        }

        patient.setMedicalHistory(updatedHistory);
        return updatePatient(patient);
    }

    public boolean assignDoctorsToPatient(int patientId) {
        Patient patient = getPatientById(patientId);
        if (patient == null) {
            System.out.println("Patient not found.");
            return false;
        }

        if (doctorAssignmentService.assignDoctorsToPatient(patient)) {
            System.out.println("\n✓ Doctors successfully assigned to patient!");
            return true;
        } else {
            System.out.println("\n✗ Could not assign doctors to patient.");
            return false;
        }
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

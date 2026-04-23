package service;

import dao.DoctorDAOImpl;
import dao.PatientDAOImpl;
import model.Doctor;
import model.Patient;
import util.SymptomUtils;

import java.util.List;

/**
 * Service for assigning doctors to patients based on medical history and symptoms.
 */
public class DoctorAssignmentService {
    private final DoctorService doctorService;
    private final PatientDAOImpl patientDAO;

    public DoctorAssignmentService() {
        this.doctorService = new DoctorService();
        this.patientDAO = new PatientDAOImpl();
    }

    /**
     * Assign doctors to a patient based on their medical history (symptoms).
     *
     * @param patient the patient to assign doctors to
     * @return true if assignment was successful
     */
    public boolean assignDoctorsToPatient(Patient patient) {
        if (patient == null || patient.getId() == null) {
            System.err.println("Invalid patient.");
            return false;
        }

        String medicalHistory = patient.getMedicalHistory();
        if (medicalHistory == null || medicalHistory.trim().isEmpty()) {
            System.out.println("No medical history found for patient. Doctors cannot be assigned.");
            return false;
        }

        // Parse symptoms from medical history (comma-separated)
        String[] symptoms = medicalHistory.split(",");
        boolean anyAssigned = false;

        for (String symptom : symptoms) {
            symptom = symptom.trim();
            if (!symptom.isEmpty()) {
                String specialty = SymptomUtils.mapSymptomToSpecialty(symptom);
                List<Doctor> availableDoctors = doctorService.getDoctorsBySpecialty(specialty);

                if (!availableDoctors.isEmpty()) {
                    // Assign the first available doctor with matching specialty
                    Doctor doctor = availableDoctors.get(0);
                    if (patientDAO.assignDoctorToPatient(patient.getId(), doctor.getId())) {
                        System.out.println("✓ Assigned Dr. " + doctor.getName() + " (" + doctor.getSpecialty() + ") for symptom: " + symptom);
                        anyAssigned = true;
                    }
                } else {
                    System.out.println("✗ No doctor available for specialty: " + specialty + " (for symptom: " + symptom + ")");
                }
            }
        }

        if (anyAssigned) {
            // Reload patient to get updated doctor list
            Patient updatedPatient = patientDAO.getPatientById(patient.getId());
            patient.setDoctorList(updatedPatient.getDoctorList());
        }

        return anyAssigned;
    }

    /**
     * Get available doctors for a specific symptom.
     *
     * @param symptom the symptom
     * @return list of doctors with the required specialty
     */
    public List<Doctor> getAvailableDoctorsForSymptom(String symptom) {
        if (symptom == null || symptom.trim().isEmpty()) {
            return List.of();
        }

        String specialty = SymptomUtils.mapSymptomToSpecialty(symptom);
        return doctorService.getDoctorsBySpecialty(specialty);
    }
}

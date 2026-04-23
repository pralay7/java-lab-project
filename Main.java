import model.Doctor;
import model.Patient;
import service.DoctorService;
import service.PatientService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Application entry point for the Hospital Management System.
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final PatientService patientService = new PatientService();
    private static final DoctorService doctorService = new DoctorService();

    public static void main(String[] args) {
        System.out.println("=== Hospital Management System ===");
        while (true) {
            printMainMenu();
            int choice = readIntegerInput("Choose an option: ");
            switch (choice) {
                case 1 -> patientMenu();
                case 2 -> doctorMenu();
                case 3 -> exitApplication();
                default -> System.out.println("Invalid selection. Please choose 1, 2, or 3.");
            }
        }
    }

    // ─────────────────────────────────────────────
    //  MAIN MENU
    // ─────────────────────────────────────────────

    private static void printMainMenu() {
        System.out.println("\n╔══════════════════════════╗");
        System.out.println("║       MAIN MENU          ║");
        System.out.println("╠══════════════════════════╣");
        System.out.println("║ 1. Patient Management    ║");
        System.out.println("║ 2. Doctor Management     ║");
        System.out.println("║ 3. Exit                  ║");
        System.out.println("╚══════════════════════════╝");
    }

    // ─────────────────────────────────────────────
    //  PATIENT MENU
    // ─────────────────────────────────────────────

    private static void patientMenu() {
        while (true) {
            printPatientMenu();
            int choice = readIntegerInput("Choose an option: ");
            switch (choice) {
                case 1 -> addPatient();
                case 2 -> viewAllPatients();
                case 3 -> searchPatientById();
                case 4 -> updatePatient();
                case 5 -> updateMedicalHistoryWithSymptoms();
                case 6 -> assignDoctorsToPatient();
                case 7 -> deletePatient();
                case 8 -> { return; } // back to main menu
                default -> System.out.println("Invalid selection. Please choose a number between 1 and 8.");
            }
        }
    }

    private static void printPatientMenu() {
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║         PATIENT MANAGEMENT            ║");
        System.out.println("╠═══════════════════════════════════════╣");
        System.out.println("║ 1. Add a new patient                  ║");
        System.out.println("║ 2. View all patients                  ║");
        System.out.println("║ 3. Search patient by ID               ║");
        System.out.println("║ 4. Update patient details             ║");
        System.out.println("║ 5. Add medical history (symptoms)     ║");
        System.out.println("║ 6. Assign doctors to patient          ║");
        System.out.println("║ 7. Delete a patient                   ║");
        System.out.println("║ 8. Back to main menu                  ║");
        System.out.println("╚═══════════════════════════════════════╝");
    }

    private static void addPatient() {
        System.out.println("\n=== Add New Patient ===");
        try {
            String name = readStringInput("Name: ");
            int age = readIntegerInput("Age: ");
            if(age<0){
                System.err.println("Age cannot be negative.");
                return;
            }
            String gender = readStringInput("Gender (M/F/Other): ");
            String contactNumber = readStringInput("Contact Number: ");

            System.out.println("\nWould you like to add medical history/symptoms now?");
            System.out.println("1. Yes, select from symptoms list");
            System.out.println("2. No, skip for now");
            int choice = readIntegerInput("Choose: ");

            String medicalHistory = "";
            if (choice == 1) {
                String symptom = patientService.displaySymptomMenu(scanner);
                if (symptom != null) {
                    medicalHistory = symptom;
                    // Allow adding more symptoms
                    while (true) {
                        System.out.println("\nAdd another symptom?");
                        System.out.println("1. Yes");
                        System.out.println("2. No");
                        int moreChoice = readIntegerInput("Choose: ");
                        if (moreChoice != 1) break;
                        String extra = patientService.displaySymptomMenu(scanner);
                        if (extra != null) {
                            medicalHistory += ", " + extra;
                        }
                    }
                }
            }

            Patient patient = new Patient(name, age, gender, contactNumber,
                    medicalHistory.isEmpty() ? null : medicalHistory);
            boolean success = patientService.addPatient(patient);
            System.out.println(success ? "Patient added successfully." : "Failed to add patient.");
        } catch (InputMismatchException e) {
            System.err.println("Invalid entry. Please enter valid data.");
            scanner.nextLine();
        }
    }

    private static void viewAllPatients() {
        System.out.println("\n=== All Patients ===");
        List<Patient> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
            return;
        }
        patients.forEach(p -> System.out.println("\n" + p));
    }

    private static void searchPatientById() {
        System.out.println("\n=== Search Patient by ID ===");
        int id = readIntegerInput("Enter patient ID: ");
        Patient patient = patientService.getPatientById(id);
        if (patient == null) {
            System.out.println("No patient found with ID " + id + ".");
        } else {
            System.out.println("\n" + patient);
        }
    }

    private static void updatePatient() {
        System.out.println("\n=== Update Patient Details ===");
        int id = readIntegerInput("Enter patient ID to update: ");
        Patient existing = patientService.getPatientById(id);
        if (existing == null) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.println("Leave a field blank to keep current value.");
        String name = readOptionalStringInput("Name [" + existing.getName() + "]: ");
        Integer age = readOptionalIntegerInput("Age [" + existing.getAge() + "]: ");
        if(age != null && age < 0){
            System.err.println("Age cannot be negative.");
            return;
        }
        String gender = readOptionalStringInput("Gender [" + existing.getGender() + "]: ");
        String contactNumber = readOptionalStringInput("Contact Number [" + existing.getContactNumber() + "]: ");

        if (!name.isBlank())         existing.setName(name);
        if (age != null)             existing.setAge(age);
        if (!gender.isBlank())       existing.setGender(gender);
        if (!contactNumber.isBlank()) existing.setContactNumber(contactNumber);

        boolean success = patientService.updatePatient(existing);
        System.out.println(success ? "Patient updated successfully." : "Failed to update patient.");
    }

    private static void updateMedicalHistoryWithSymptoms() {
        System.out.println("\n=== Add Medical History (Symptoms) ===");
        int patientId = readIntegerInput("Enter patient ID: ");

        Patient patient = patientService.getPatientById(patientId);
        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }
        System.out.println("Current medical history: " +
                (patient.getMedicalHistory() == null ? "None" : patient.getMedicalHistory()));

        while (true) {
            String symptom = patientService.displaySymptomMenu(scanner);
            if (symptom != null) {
                if (patientService.updateMedicalHistoryWithSymptoms(patientId, symptom, scanner)) {
                    System.out.println("✓ Medical history updated with symptom: " + symptom);
                } else {
                    System.out.println("✗ Failed to update medical history.");
                }
            } else {
                System.out.println("No symptom selected.");
            }

            System.out.println("\nWould you like to add another symptom?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            int choice = readIntegerInput("Choose: ");
            if (choice != 1) break;
        }
    }

    private static void assignDoctorsToPatient() {
        System.out.println("\n=== Assign Doctors to Patient ===");
        int patientId = readIntegerInput("Enter patient ID: ");
        Patient patient = patientService.getPatientById(patientId);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.println("\nPatient: " + patient.getName());
        System.out.println("Medical History: " +
                (patient.getMedicalHistory() == null ? "None" : patient.getMedicalHistory()));

        if (patient.getMedicalHistory() == null || patient.getMedicalHistory().trim().isEmpty()) {
            System.out.println("\n✗ No medical history found. Please add symptoms first before assigning doctors.");
            return;
        }

        if (patientService.assignDoctorsToPatient(patientId)) {
            Patient updatedPatient = patientService.getPatientById(patientId);
            if (updatedPatient != null && updatedPatient.hasDoctors()) {
                System.out.println("\nAssigned Doctors:");
                updatedPatient.getDoctorList().forEach(doctor ->
                        System.out.println("  - Dr. " + doctor.getName()
                                + " | Specialty: " + doctor.getSpecialty()
                                + " | Degree: " + doctor.getDegree()
                                + " | Contact: " + doctor.getContactNumber()));
            }
        }
    }

    private static void deletePatient() {
        System.out.println("\n=== Delete Patient ===");
        int id = readIntegerInput("Enter patient ID to delete: ");
        boolean success = patientService.deletePatient(id);
        System.out.println(success ? "Patient deleted successfully."
                : "Failed to delete patient or patient not found.");
    }

    // ─────────────────────────────────────────────
    //  DOCTOR MENU
    // ─────────────────────────────────────────────

    private static void doctorMenu() {
        while (true) {
            printDoctorMenu();
            int choice = readIntegerInput("Choose an option: ");
            switch (choice) {
                case 1 -> addDoctor();
                case 2 -> viewAllDoctors();
                case 3 -> searchDoctorById();
                case 4 -> updateDoctor();
                case 5 -> deleteDoctor();
                case 6 -> { return; } // back to main menu
                default -> System.out.println("Invalid selection. Please choose a number between 1 and 6.");
            }
        }
    }

    private static void printDoctorMenu() {
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║          DOCTOR MANAGEMENT            ║");
        System.out.println("╠═══════════════════════════════════════╣");
        System.out.println("║ 1. Add a new doctor                   ║");
        System.out.println("║ 2. View all doctors                   ║");
        System.out.println("║ 3. Search doctor by ID                ║");
        System.out.println("║ 4. Update doctor details              ║");
        System.out.println("║ 5. Delete a doctor                    ║");
        System.out.println("║ 6. Back to main menu                  ║");
        System.out.println("╚═══════════════════════════════════════╝");
    }

    private static void addDoctor() {
        System.out.println("\n=== Add New Doctor ===");
        String name = readStringInput("Full Name (e.g. John Smith): ");
        String degree = readStringInput("Degree (e.g. MD, MBBS, PhD): ");
        String specialty = selectSpecialty();
        String contactNumber = readStringInput("Contact Number: ");

        Doctor doctor = new Doctor(name, degree, specialty, contactNumber);
        boolean success = doctorService.addDoctor(doctor);
        System.out.println(success ? "Doctor added successfully." : "Failed to add doctor.");
    }

    private static String selectSpecialty() {
        String[] specialties = {
            "General Practitioner",
            "Cardiologist",
            "Neurologist",
            "Dermatologist",
            "Orthopedist",
            "Pediatrician",
            "Psychiatrist",
            "Oncologist",
            "Gastroenterologist",
            "Endocrinologist"
        };

        System.out.println("\n=== Select Specialty ===");
        for (int i = 0; i < specialties.length; i++) {
            System.out.println((i + 1) + ". " + specialties[i]);
        }
        System.out.println((specialties.length + 1) + ". Enter custom specialty");

        while (true) {
            int choice = readIntegerInput("Choose specialty: ");
            if (choice >= 1 && choice <= specialties.length) {
                return specialties[choice - 1];
            } else if (choice == specialties.length + 1) {
                return readStringInput("Enter custom specialty: ");
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void viewAllDoctors() {
        System.out.println("\n=== All Doctors ===");
        List<Doctor> doctors = doctorService.getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("No doctors found.");
            return;
        }
        doctors.forEach(d -> System.out.println("\n" + d));
    }

    private static void searchDoctorById() {
        System.out.println("\n=== Search Doctor by ID ===");
        int id = readIntegerInput("Enter doctor ID: ");
        Doctor doctor = doctorService.getDoctorById(id);
        if (doctor == null) {
            System.out.println("No doctor found with ID " + id + ".");
        } else {
            System.out.println("\n" + doctor);
            if (!doctor.getPatientIds().isEmpty()) {
                System.out.println("  Patient IDs: " + doctor.getPatientIds());
            } else {
                System.out.println("  Patients: None assigned");
            }
        }
    }

    private static void updateDoctor() {
        System.out.println("\n=== Update Doctor Details ===");
        int id = readIntegerInput("Enter doctor ID to update: ");
        Doctor existing = doctorService.getDoctorById(id);
        if (existing == null) {
            System.out.println("Doctor not found.");
            return;
        }

        System.out.println("Current details: " + existing);
        System.out.println("Leave a field blank to keep current value.");

        String name = readOptionalStringInput("Name [" + existing.getName() + "]: ");
        String degree = readOptionalStringInput("Degree [" + existing.getDegree() + "]: ");

        System.out.println("\nUpdate specialty?");
        System.out.println("1. Yes, select new specialty");
        System.out.println("2. No, keep current (" + existing.getSpecialty() + ")");
        int specialtyChoice = readIntegerInput("Choose: ");
        String specialty = (specialtyChoice == 1) ? selectSpecialty() : "";

        String contactNumber = readOptionalStringInput("Contact Number [" + existing.getContactNumber() + "]: ");

        if (!name.isBlank())          existing.setName(name);
        if (!degree.isBlank())        existing.setDegree(degree);
        if (!specialty.isBlank())     existing.setSpecialty(specialty);
        if (!contactNumber.isBlank()) existing.setContactNumber(contactNumber);

        boolean success = doctorService.updateDoctor(existing);
        System.out.println(success ? "Doctor updated successfully." : "Failed to update doctor.");
    }

    private static void deleteDoctor() {
        System.out.println("\n=== Delete Doctor ===");
        int id = readIntegerInput("Enter doctor ID to delete: ");
        boolean success = doctorService.deleteDoctor(id);
        System.out.println(success ? "Doctor deleted successfully."
                : "Failed to delete doctor or doctor not found.");
    }

    // ─────────────────────────────────────────────
    //  UTILITY
    // ─────────────────────────────────────────────

    private static void exitApplication() {
        System.out.println("Exiting system. Goodbye.");
        scanner.close();
        System.exit(0);
    }

    private static int readIntegerInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.err.println("Please enter a valid integer.");
            }
        }
    }

    private static String readStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static String readOptionalStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static Integer readOptionalIntegerInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return null;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.err.println("Please enter a valid number or leave blank.");
            }
        }
    }
}

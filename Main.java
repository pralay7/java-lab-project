import model.Patient;
import service.PatientService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Application entry point for the Patient Management System.
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final PatientService patientService = new PatientService();

    public static void main(String[] args) {
        System.out.println("=== Patient Management System ===");
        while (true) {
            printMenu();
            int choice = readIntegerInput("Choose an option: ");
            switch (choice) {
                case 1 -> addPatient();
                case 2 -> viewAllPatients();
                case 3 -> searchPatientById();
                case 4 -> updatePatient();
                case 5 -> deletePatient();
                case 6 -> exitApplication();
                default -> System.out.println("Invalid selection. Please choose a number between 1 and 6.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Add a new patient");
        System.out.println("2. View all patients");
        System.out.println("3. Search patient by ID");
        System.out.println("4. Update a patient's details");
        System.out.println("5. Delete a patient");
        System.out.println("6. Exit");
    }

    private static void addPatient() {
        System.out.println("\n=== Add New Patient ===");
        try {
            String name = readStringInput("Name: ");
            int age = readIntegerInput("Age: ");
            String gender = readStringInput("Gender: ");
            String contactNumber = readStringInput("Contact Number: ");
            String medicalHistory = readStringInput("Medical History (optional): ");

            Patient patient = new Patient(name, age, gender, contactNumber, medicalHistory);
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
        patients.forEach(System.out::println);
    }

    private static void searchPatientById() {
        System.out.println("\n=== Search Patient by ID ===");
        int id = readIntegerInput("Enter patient ID: ");
        Patient patient = patientService.getPatientById(id);
        if (patient == null) {
            System.out.println("No patient found with ID " + id + ".");
        } else {
            System.out.println(patient);
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
        String gender = readOptionalStringInput("Gender [" + existing.getGender() + "]: ");
        String contactNumber = readOptionalStringInput("Contact Number [" + existing.getContactNumber() + "]: ");
        String medicalHistory = readOptionalStringInput("Medical History [" + (existing.getMedicalHistory() == null ? "None" : existing.getMedicalHistory()) + "]: ");

        if (!name.isBlank()) {
            existing.setName(name);
        }
        if (age != null) {
            existing.setAge(age);
        }
        if (!gender.isBlank()) {
            existing.setGender(gender);
        }
        if (!contactNumber.isBlank()) {
            existing.setContactNumber(contactNumber);
        }
        if (!medicalHistory.isBlank()) {
            existing.setMedicalHistory(medicalHistory);
        }

        boolean success = patientService.updatePatient(existing);
        System.out.println(success ? "Patient updated successfully." : "Failed to update patient.");
    }

    private static void deletePatient() {
        System.out.println("\n=== Delete Patient ===");
        int id = readIntegerInput("Enter patient ID to delete: ");
        boolean success = patientService.deletePatient(id);
        System.out.println(success ? "Patient deleted successfully." : "Failed to delete patient or patient not found.");
    }

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

package util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for managing symptoms and mapping them to doctor specialties.
 */
public class SymptomUtils {

    // List of common symptoms
    public static final List<String> COMMON_SYMPTOMS = Arrays.asList(
            "Fever",
            "Cough",
            "Headache",
            "Chest Pain",
            "Nausea",
            "Fatigue",
            "Allergies",
            "High Blood Pressure",
            "Dizziness",
            "Throat Pain",
            "Back Pain",
            "Shortness of Breath"
    );

    // Mapping of symptoms to doctor specialties
    private static final Map<String, String> SYMPTOM_TO_SPECIALTY = new HashMap<>();

    static {
        // Initialize symptom to specialty mapping
        SYMPTOM_TO_SPECIALTY.put("Fever", "General Practitioner");
        SYMPTOM_TO_SPECIALTY.put("Cough", "General Practitioner");
        SYMPTOM_TO_SPECIALTY.put("Headache", "Neurologist");
        SYMPTOM_TO_SPECIALTY.put("Chest Pain", "Cardiologist");
        SYMPTOM_TO_SPECIALTY.put("Nausea", "General Practitioner");
        SYMPTOM_TO_SPECIALTY.put("Fatigue", "General Practitioner");
        SYMPTOM_TO_SPECIALTY.put("Allergies", "Dermatologist");
        SYMPTOM_TO_SPECIALTY.put("High Blood Pressure", "Cardiologist");
        SYMPTOM_TO_SPECIALTY.put("Dizziness", "Neurologist");
        SYMPTOM_TO_SPECIALTY.put("Throat Pain", "General Practitioner");
        SYMPTOM_TO_SPECIALTY.put("Back Pain", "Orthopedist");
        SYMPTOM_TO_SPECIALTY.put("Shortness of Breath", "Cardiologist");
    }


    public static String mapSymptomToSpecialty(String symptom) {
        return SYMPTOM_TO_SPECIALTY.getOrDefault(symptom, "General Practitioner");
    }

    
    public static String displaySymptomMenu() {
        System.out.println("\n=== Select a Symptom ===");
        for (int i = 0; i < COMMON_SYMPTOMS.size(); i++) {
            System.out.println((i + 1) + ". " + COMMON_SYMPTOMS.get(i));
        }
        System.out.println((COMMON_SYMPTOMS.size() + 1) + ". Skip");
        return null; // Will be handled by calling code
    }

   
    public static String getSymptomByIndex(int index) {
        if (index > 0 && index <= COMMON_SYMPTOMS.size()) {
            return COMMON_SYMPTOMS.get(index - 1);
        }
        return null;
    }

    
    public static boolean isSkipOption(int index) {
        return index == COMMON_SYMPTOMS.size() + 1;
    }

    
    public static String getAllSymptomsAsString() {
        return String.join(", ", COMMON_SYMPTOMS);
    }
}

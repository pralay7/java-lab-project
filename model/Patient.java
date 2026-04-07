package model;

/**
 * POJO representing a patient in the system.
 */
public class Patient {
    private Integer id;
    private String name;
    private Integer age;
    private String gender;
    private String contactNumber;
    private String medicalHistory;

    public Patient() {
    }

    public Patient(String name, Integer age, String gender, String contactNumber, String medicalHistory) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.medicalHistory = medicalHistory;
    }

    public Patient(Integer id, String name, Integer age, String gender, String contactNumber, String medicalHistory) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.medicalHistory = medicalHistory;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %d | Name: %s | Age: %d | Gender: %s | Contact: %s | Medical History: %s",
                id, name, age, gender, contactNumber, medicalHistory == null ? "None" : medicalHistory);
    }
}

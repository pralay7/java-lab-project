package model;

import java.util.ArrayList;
import java.util.List;

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
    private List<Doctor> doctorList;

    public Patient() {
        this.doctorList = new ArrayList<>();
    }

    public Patient(String name, Integer age, String gender, String contactNumber, String medicalHistory) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.medicalHistory = medicalHistory;
        this.doctorList = new ArrayList<>();
    }

    public Patient(Integer id, String name, Integer age, String gender, String contactNumber, String medicalHistory) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.medicalHistory = medicalHistory;
        this.doctorList = new ArrayList<>();
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

    public List<Doctor> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(List<Doctor> doctorList) {
        this.doctorList = doctorList;
    }

    public void addDoctor(Doctor doctor) {
        if (!this.doctorList.contains(doctor)) {
            this.doctorList.add(doctor);
        }
    }

    public void removeDoctor(Doctor doctor) {
        this.doctorList.remove(doctor);
    }

    public boolean hasDoctors() {
        return !this.doctorList.isEmpty();
    }

    @Override
    public String toString() {
        String doctorInfo = this.doctorList.isEmpty() ? "to be assigned" : 
                           this.doctorList.stream()
                                   .map(d -> d.getName() + " (" + d.getSpecialty() + ")")
                                   .reduce((a, b) -> a + ", " + b)
                                   .orElse("to be assigned");
        
        return String.format(
                "ID: %d | Name: %s | Age: %d | Gender: %s | Contact: %s | Medical History: %s | Doctors: %s",
                id, name, age, gender, contactNumber, medicalHistory == null ? "None" : medicalHistory, doctorInfo);
    }
}

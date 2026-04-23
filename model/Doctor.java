package model;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO representing a doctor in the system.
 */
public class Doctor {
    private Integer id;
    private String name;
    private String degree;
    private String specialty;
    private String contactNumber;
    private List<Integer> patientIds;

    public Doctor() {
        this.patientIds = new ArrayList<>();
    }

    public Doctor(String name, String degree, String specialty, String contactNumber) {
        this.name = name;
        this.degree = degree;
        this.specialty = specialty;
        this.contactNumber = contactNumber;
        this.patientIds = new ArrayList<>();
    }

    public Doctor(Integer id, String name, String degree, String specialty, String contactNumber) {
        this.id = id;
        this.name = name;
        this.degree = degree;
        this.specialty = specialty;
        this.contactNumber = contactNumber;
        this.patientIds = new ArrayList<>();
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

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public List<Integer> getPatientIds() {
        return patientIds;
    }

    public void setPatientIds(List<Integer> patientIds) {
        this.patientIds = patientIds;
    }

    public void addPatientId(Integer patientId) {
        if (!this.patientIds.contains(patientId)) {
            this.patientIds.add(patientId);
        }
    }

    public void removePatientId(Integer patientId) {
        this.patientIds.remove(patientId);
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %d | Name: %s | Degree: %s | Specialty: %s | Contact: %s | Patients: %s",
                id, name, degree, specialty, contactNumber, patientIds.isEmpty() ? "None" : patientIds.size() + " patients");
    }
}

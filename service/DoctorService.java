package service;

import dao.DoctorDAO;
import dao.DoctorDAOImpl;
import model.Doctor;

import java.util.List;

/**
 * Business service layer for doctor operations and validation.
 */
public class DoctorService {
    private final DoctorDAO doctorDAO;

    public DoctorService() {
        this.doctorDAO = new DoctorDAOImpl();
    }

    public boolean addDoctor(Doctor doctor) {
        if (doctor == null) {
            return false;
        }
        if (!isValidDoctor(doctor)) {
            return false;
        }
        return doctorDAO.addDoctor(doctor);
    }

    public Doctor getDoctorById(int id) {
        if (id <= 0) {
            return null;
        }
        return doctorDAO.getDoctorById(id);
    }

    public List<Doctor> getAllDoctors() {
        return doctorDAO.getAllDoctors();
    }

    public List<Doctor> getDoctorsBySpecialty(String specialty) {
        if (specialty == null || specialty.trim().isEmpty()) {
            return List.of();
        }
        return doctorDAO.getDoctorsBySpecialty(specialty);
    }

    public boolean updateDoctor(Doctor doctor) {
        if (doctor == null || doctor.getId() == null || doctor.getId() <= 0) {
            return false;
        }
        if (!isValidDoctor(doctor)) {
            return false;
        }
        return doctorDAO.updateDoctor(doctor);
    }

    public boolean deleteDoctor(int id) {
        if (id <= 0) {
            return false;
        }
        return doctorDAO.deleteDoctor(id);
    }

    private boolean isValidDoctor(Doctor doctor) {
        if (doctor.getName() == null || doctor.getName().trim().isEmpty()) {
            System.err.println("Doctor name is required.");
            return false;
        }
        if (doctor.getDegree() == null || doctor.getDegree().trim().isEmpty()) {
            System.err.println("Doctor degree is required.");
            return false;
        }
        if (doctor.getSpecialty() == null || doctor.getSpecialty().trim().isEmpty()) {
            System.err.println("Doctor specialty is required.");
            return false;
        }
        if (doctor.getContactNumber() == null || doctor.getContactNumber().trim().isEmpty()) {
            System.err.println("Doctor contact number is required.");
            return false;
        }
        return true;
    }
}

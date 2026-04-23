@echo off
echo === Compiling Hospital Management System ===
javac -cp ".;sqlite-jdbc-3.42.0.0.jar" Main.java model\Patient.java model\Doctor.java dao\DoctorDAO.java dao\PatientDAO.java dao\DoctorDAOImpl.java dao\PatientDAOImpl.java service\DoctorService.java service\PatientService.java service\DoctorAssignmentService.java util\DatabaseConnection.java util\SymptomUtils.java 2>&1
if %errorlevel% == 0 (
    echo.
    echo === COMPILE SUCCESS ===
    echo.
    echo === Running Tests via input.txt ===
    java -cp ".;sqlite-jdbc-3.42.0.0.jar" Main < test_input.txt
) else (
    echo.
    echo === COMPILE FAILED ===
)

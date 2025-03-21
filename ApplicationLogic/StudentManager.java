package applicationlogic;

import DataPersistence.StudentRecord;

public class StudentManager {

    // Initializes the student records by loading from file
    public static void initialize() {
        StudentRecord.loadStudentsFromFile();
    }
    
    //  Adds a new student record.
    public static StudentRecord addStudent(String firstName, String lastName, String birthdate,
                                             String address, String guardian, String regularContact,
                                             String emergencyContact) {
        return new StudentRecord(firstName, lastName, birthdate, address, guardian, regularContact, emergencyContact);
    }
    
    // Finds a student record by first and last name.
    public static StudentRecord findStudent(String firstName, String lastName) {
        return StudentRecord.findStudent(firstName, lastName);
    }
    
    // Update: Updates an existing student's details.
    public static void updateStudent(String firstName, String lastName, String newBirthdate,
                                     String newAddress, String newGuardian, String newRegularContact,
                                     String newEmergencyContact) {
        StudentRecord student = findStudent(firstName, lastName);
        if (student != null) {
            student.updateStudent(newBirthdate, newAddress, newGuardian, newRegularContact, newEmergencyContact);
        } else {
            System.out.println("Student not found.");
        }
    }
    
    // Delete: Removes a student record.
    public static void deleteStudent(String firstName, String lastName) {
        StudentRecord student = findStudent(firstName, lastName);
        if (student != null) {
            student.deleteStudent();
        } else {
            System.out.println("Student not found.");
        }
    }
    
    // Display an individual student record's details
    public static void displayStudentRecord(String firstName, String lastName) {
        StudentRecord student = findStudent(firstName, lastName);
        if (student != null) {
            System.out.println("Student Details:");
            System.out.println("Name: " + student.getFirstName() + " " + student.getLastName());
            System.out.println("Birthdate: " + student.getBirthdate());
            System.out.println("Address: " + student.getAddress());
            System.out.println("Guardian: " + student.getGuardian());
            System.out.println("Regular Contact: " + student.getRegularContact());
            System.out.println("Emergency Contact: " + student.getEmergencyContact());
        } else {
            System.out.println("Student not found.");
        }
    }
    
    //Lists all students
    public static void listStudents() {
        System.out.println("Listing Students:");
        for (StudentRecord sr : StudentRecord.getAllStudents()) {
            System.out.println(sr.getLastName() + ", " + sr.getFirstName());
        }
    }
}
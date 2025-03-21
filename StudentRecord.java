import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The StudentRecord class handles adding, updating, deleting, saving, and loading
 * student data.
 */
public class StudentRecord {

    private static final String STUDENT_LIST_FILE = "Data/Student/Records/StudentsList.txt";

    /**
     * Add a new student record.
     */
    public static void addStudent(String firstName, String lastName, String birthdate, String address, String guardian, String regContact, String emergencyContact) {
        new Student(firstName, lastName, birthdate, address, guardian, regContact, emergencyContact);
        System.out.println("Student added: " + firstName + " " + lastName);
    }

    /**
     * Update student information.
     */
    public static boolean updateStudent(String oldFirstName, String oldLastName, String newFirstName, String newLastName, String birthdate, String address, String guardian, String regContact, String emergencyContact) {
        Student student = Student.findStudent(oldFirstName, oldLastName);
        if (student != null) {
            // Remove old record
            deleteStudent(oldFirstName, oldLastName);

            // Add updated record
            addStudent(newFirstName, newLastName, birthdate, address, guardian, regContact, emergencyContact);
            System.out.println("Student updated: " + newFirstName + " " + newLastName);
            return true;
        }
        return false;
    }

    /**
     * Delete a student record by name.
     */
    public static boolean deleteStudent(String firstName, String lastName) {
        Student student = Student.findStudent(firstName, lastName);
        if (student != null) {
            File studentFile = student.getSFile();
            if (studentFile.exists()) {
                studentFile.delete();
            }
            Student.studentList.remove(student);
            rewriteStudentListFile();
            System.out.println("Student deleted: " + firstName + " " + lastName);
            return true;
        }
        return false;
    }

    /**
     * Save all current students to file (StudentsList.txt).
     */
    public static void saveStudentsToFile() {
        rewriteStudentListFile();
        System.out.println("All students saved to " + STUDENT_LIST_FILE);
    }

    /**
     * Load students from the StudentsList.txt file.
     */
    public static void loadStudentsFromFile() {
        Student.loadExistingStudnets();
        System.out.println("Students loaded from " + STUDENT_LIST_FILE);
    }

    /**
     * Rewrite StudentsList.txt with the current student list in memory.
     */
    private static void rewriteStudentListFile() {
        File studentFile = new File(STUDENT_LIST_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(studentFile))) {
            for (Student student : Student.studentList) {
                writer.write(student.getFirstName() + " " + student.getLastName() + " "
                        + student.getBirthdate() + " " + student.getAddress() + " "
                        + student.getGuardian() + " " + student.getRegContact() + " "
                        + student.getEContact());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to student list file: " + e.getMessage());
        }
    }

    /**
     * Display all students in the console (for debug/testing).
     */
    public static void displayAllStudents() {
        for (Student s : Student.studentList) {
            System.out.println(s.getFirstName() + " " + s.getLastName() + " | Birthdate: " + s.getBirthdate() + " | Address: " + s.getAddress());
        }
    }
}


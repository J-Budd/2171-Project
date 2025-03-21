package DataPersistence;

import java.io.*;
import java.util.*;

public class StudentRecord {

    private String firstName;
    private String lastName;
    private String birthdate;
    private String address;
    private String guardian;
    private String regularContact;
    private String emergencyContact;
    

    private static ArrayList<StudentRecord> studentList = new ArrayList<>();
    
    
    private static final String MASTER_FILE = "Data/Student/Records/StudentRecord.txt";

    //constructor for creating a new student record.

    public StudentRecord(String firstName, String lastName, String birthdate, String address,
                         String guardian, String regularContact, String emergencyContact) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.address = address;
        this.guardian = guardian;
        this.regularContact = regularContact;
        this.emergencyContact = emergencyContact;
        createStudentFile();
        studentList.add(this);
        appendToMasterFile();
    }
    
    private StudentRecord(String firstName, String lastName, String birthdate, String address,
                        String guardian, String regularContact, String emergencyContact, boolean isLoading) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.address = address;
        this.guardian = guardian;
        this.regularContact = regularContact;
        this.emergencyContact = emergencyContact;
        studentList.add(this);
    }
    

    private void createStudentFile() {
        String fileName = "Data/Student/Records/" + lastName + firstName + ".txt";
        File directory = new File("Data/Student/Records/");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(fileName);
        try {
            if (file.createNewFile()) {
                System.out.println("File created for: " + firstName + " " + lastName);
            } else {
                System.out.println("File already exists for: " + firstName + " " + lastName);
            }
        } catch (IOException e) {
            System.out.println("Error creating file for " + firstName + " " + lastName + ": " + e.getMessage());
        }
    }
    
    // Add student record to MasterFile
    private void appendToMasterFile() {
        File studentFile = new File(MASTER_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(studentFile, true))) {
            writer.write(lastName + " " + firstName + " " + birthdate + " " + address + " " +
                         guardian + " " + regularContact + " " + emergencyContact);
            writer.newLine();
            System.out.println("Student saved to master file.");
        } catch (IOException e) {
            System.out.println("Error saving student to master file: " + e.getMessage());
        }
    }
    
    public static void loadStudentsFromFile() {
        studentList.clear();
        File masterFile = new File(MASTER_FILE);
        if (!masterFile.exists()){
            System.out.println("Master file does not exist.");
            return;
        }
        try (Scanner scanner = new Scanner(masterFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] tokens = line.split(" ");
                if (tokens.length >= 7) {
                    new StudentRecord(tokens[1], tokens[0], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6], true);
                }
            }
            System.out.println("Loaded students from master file.");
        } catch (IOException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
    }
    

    public static List<StudentRecord> getAllStudents() {
        return new ArrayList<>(studentList);
    }
    
    //Accessors 
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getBirthdate() {
        return birthdate;
    }
    
    public String getAddress() {
        return address;
    }
    
    public String getGuardian() {
        return guardian;
    }
    
    public String getRegularContact() {
        return regularContact;
    }
    
    public String getEmergencyContact() {
        return emergencyContact;
    }

    public static StudentRecord findStudent(String firstName, String lastName) {
        for (StudentRecord sr : studentList) {
            if (sr.firstName.equals(firstName) && sr.lastName.equals(lastName)) {
                return sr;
            }
        }
        return null;
    }
    
    public void updateStudent(String newBirthdate, String newAddress, String newGuardian,
                              String newRegularContact, String newEmergencyContact) {
        this.birthdate = newBirthdate;
        this.address = newAddress;
        this.guardian = newGuardian;
        this.regularContact = newRegularContact;
        this.emergencyContact = newEmergencyContact;
        rewriteMasterFile();
    }
    

    public void deleteStudent() {
        studentList.remove(this);
        String fileName = "Data/Student/Records/" + lastName + firstName + ".txt";
        File file = new File(fileName);
        if (file.exists() && file.delete()) {
            System.out.println("Deleted student file: " + fileName);
        } else {
            System.out.println("Failed to delete student file: " + fileName);
        }
        rewriteMasterFile();
    }
    

    private static void rewriteMasterFile() {
        File studentFile = new File(MASTER_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(studentFile))) {
            for (StudentRecord sr : studentList) {
                writer.write(sr.lastName + " " + sr.firstName + " " + sr.birthdate + " " +
                             sr.address + " " + sr.guardian + " " + sr.regularContact + " " +
                             sr.emergencyContact);
                writer.newLine();
            }
            System.out.println("Master file updated.");
        } catch (IOException e) {
            System.out.println("Error rewriting master file: " + e.getMessage());
        }
    }
    
}
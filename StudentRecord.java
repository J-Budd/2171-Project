package Data_Persistence;

import java.io.*;
import java.util.*;

public class StudentRecord {

    private int id;
    private String firstName;
    private String lastName;
    private String birthdate;
    private String address;
    private String guardian;
    private String regularContact;
    private String emergencyContact;

    public static ArrayList<StudentRecord> studentList = new ArrayList<>();

    private static final String MASTER_FILE = "Data/Student/Records/StudentRecord.txt";
    private static final String STUDENTS_LIST_FILE = "Data/Student/Records/StudentsList.txt";

    // Constructor for creating a new student record
    public StudentRecord(String firstName, String lastName, String birthdate, String address,
                         String guardian, String regularContact, String emergencyContact) {
        this.id = IDGenerator.generateID();
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
        saveStudentNamesToFile();
    }

    // Constructor for loading a student record (with provided ID)
    private StudentRecord(int id, String firstName, String lastName, String birthdate, String address,
                          String guardian, String regularContact, String emergencyContact, boolean isLoading) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.address = address;
        this.guardian = guardian;
        this.regularContact = regularContact;
        this.emergencyContact = emergencyContact;
        studentList.add(this);
    }

    // Create (or ensure existence of) the individual student file
    private void createStudentFile() {
        String fileName = "Data/Student/Records/" + lastName + firstName + ".txt";
        File directory = new File("Data/Student/Records/");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Individual file created for: " + firstName + " " + lastName);
            }
        } catch (IOException e) {
            System.out.println("Error creating individual file for " + firstName + " " + lastName + ": " + e.getMessage());
        }
    }

    // Update the individual file with personal data and the latest grade/teacher info
    public void updateIndividualFile() {
        String fileName = "Data/Student/Records/" + lastName + firstName + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("ID: " + id + "\n");
            writer.write("Name: " + firstName + " " + lastName + "\n");
            writer.write("Birthdate: " + birthdate + "\n");
            writer.write("Address: " + address + "\n");
            writer.write("Guardian: " + guardian + "\n");
            writer.write("Regular Contact: " + regularContact + "\n");
            writer.write("Emergency Contact: " + emergencyContact + "\n\n");
            writer.write("----- Grade & Teacher Information -----\n");
            // Retrieve associated GradeRecord via GradeManager
            GradeRecord gr = Application_Logic.GradeManager.findGradeRecord(firstName, lastName);
            if (gr != null) {
                writer.write("Current Teacher: " + gr.getTeacherAssigned() + "\n");
                writer.write("Teacher History: " + gr.getTeacherHistory().toString() + "\n");
                writer.write("Grades:\n" + gr.getGradesTable() + "\n");
            } else {
                writer.write("No grade record available.\n");
            }
        } catch (IOException e) {
            System.out.println("Error updating individual file for " + firstName + " " + lastName + ": " + e.getMessage());
        }
    }

    private void appendToMasterFile() {
        File file = new File(MASTER_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            // Format: id|lastName|firstName|birthdate|address|guardian|regularContact|emergencyContact
            writer.write(id + "|" + lastName + "|" + firstName + "|" + birthdate + "|" + address + "|" + guardian + "|" + regularContact + "|" + emergencyContact);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error appending to master file: " + e.getMessage());
        }
    }

    public static void loadStudentsFromFile() {
        studentList.clear();
        File file = new File(MASTER_FILE);
        if (!file.exists()) {
            System.out.println("Master file not found.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("\\|");
                if (tokens.length >= 8) {
                    int id = Integer.parseInt(tokens[0]);
                    new StudentRecord(id, tokens[2], tokens[1], tokens[3], tokens[4], tokens[5], tokens[6], tokens[7], true);
                }
            }
            System.out.println("Students loaded from master file.");
        } catch (IOException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
    }

    public static List<StudentRecord> getAllStudents() {
        return new ArrayList<>(studentList);
    }

    public static StudentRecord findStudent(String firstName, String lastName) {
        for (StudentRecord sr : studentList) {
            if (sr.firstName.equals(firstName) && sr.lastName.equals(lastName)) {
                return sr;
            }
        }
        return null;
    }

    public static StudentRecord findStudentByID(int id) {
        for (StudentRecord sr : studentList) {
            if (sr.getId() == id) {
                return sr;
            }
        }
        return null;
    }

    public void updateStudent(String newFirstName, String newLastName, String newBirthdate, String newAddress,
                              String newGuardian, String newRegularContact, String newEmergencyContact) {
        this.firstName = newFirstName;
        this.lastName = newLastName;
        this.birthdate = newBirthdate;
        this.address = newAddress;
        this.guardian = newGuardian;
        this.regularContact = newRegularContact;
        this.emergencyContact = newEmergencyContact;
        rewriteMasterFile();
        saveStudentNamesToFile();
        updateIndividualFile();
    }

    public void deleteStudent() {
        studentList.remove(this);
        String fileName = "Data/Student/Records/" + lastName + firstName + ".txt";
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        rewriteMasterFile();
        saveStudentNamesToFile();
    }

    private static void rewriteMasterFile() {
        File file = new File(MASTER_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (StudentRecord sr : studentList) {
                writer.write(sr.id + "|" + sr.lastName + "|" + sr.firstName + "|" + sr.birthdate + "|" + sr.address + "|" + sr.guardian + "|" + sr.regularContact + "|" + sr.emergencyContact);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error rewriting master file: " + e.getMessage());
        }
    }

    public static void saveStudentNamesToFile() {
        File file = new File(STUDENTS_LIST_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (StudentRecord sr : studentList) {
                writer.write(sr.firstName + " " + sr.lastName);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving student names: " + e.getMessage());
        }
    }

    // Getters for the fields
    public int getId() {
        return id;
    }
    
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
}
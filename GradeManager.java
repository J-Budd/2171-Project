package Application_Logic;

import Data_Persistence.GradeRecord;
import Data_Persistence.StudentRecord;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class GradeManager {
    // List holding all GradeRecord objects.
    public static List<GradeRecord> gradeRecords = new ArrayList<>();
    private static final String GRADE_RECORD_FILE = "Data/Grades/GradeRecords.txt";

    // Add a new GradeRecord and update the student's file.
    public static void addGradeRecord(GradeRecord record) {
        gradeRecords.add(record);
        appendRecordToFile(record);
        StudentRecord sr = StudentRecord.findStudent(record.getStudentFirstName(), record.getStudentLastName());
        if (sr != null) {
            sr.updateIndividualFile();
        }
    }
    
    // Find a GradeRecord by student's first and last name.
    public static GradeRecord findGradeRecord(String studentFirstName, String studentLastName) {
        for (GradeRecord record : gradeRecords) {
            if (record.getStudentFirstName().equals(studentFirstName) &&
                record.getStudentLastName().equals(studentLastName)) {
                return record;
            }
        }
        return null;
    }
    
    // Edit a grade; update the file after modifying.
    public static boolean editGrade(String studentFirstName, String studentLastName, String subject, int term, int newGrade) {
        GradeRecord record = findGradeRecord(studentFirstName, studentLastName);
        if (record != null) {
            boolean success = record.editGrade(subject, term, newGrade);
            saveAllRecordsToFile();
            StudentRecord sr = StudentRecord.findStudent(studentFirstName, studentLastName);
            if (sr != null) {
                sr.updateIndividualFile();
            }
            return success;
        }
        return false;
    }
    
    // Remove a grade for a specific student, subject, and term.
    public static boolean removeGrade(String studentFirstName, String studentLastName, String subject, int term) {
        GradeRecord record = findGradeRecord(studentFirstName, studentLastName);
        if (record != null) {
            boolean success = record.removeGrade(subject, term);
            saveAllRecordsToFile();
            StudentRecord sr = StudentRecord.findStudent(studentFirstName, studentLastName);
            if (sr != null) {
                sr.updateIndividualFile();
            }
            return success;
        }
        return false;
    }
    
    
    // Get a specific grade.
    public static Integer getGrade(String studentFirstName, String studentLastName, String subject, int term) {
        GradeRecord record = findGradeRecord(studentFirstName, studentLastName);
        return (record != null) ? record.getGrade(subject, term) : null;
    }
    
    // Get all grade records for a teacher's students.
    public static List<GradeRecord> getGradesForTeacher(String teacherName) {
        return gradeRecords.stream()
            .filter(record -> record.getTeacherAssigned().equals(teacherName))
            .collect(Collectors.toList());
    }
    
    // ========================
    // Persistence Methods
    // ========================
    
    // Load grade records from file.
    public static void loadRecordsFromFile() {
        gradeRecords.clear();
        File file = new File(GRADE_RECORD_FILE);
        if (!file.exists()) {
            System.out.println("Grade records file does not exist.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = reader.readLine()) != null) {
                // Expected format:
                // firstName|lastName|teacherAssigned|teacherHistory|grades
                String[] tokens = line.split("\\|");
                if (tokens.length != 5) continue;
                String firstName = tokens[0];
                String lastName = tokens[1];
                String teacherAssigned = tokens[2];
                String teacherHistoryStr = tokens[3];
                String gradesStr = tokens[4];
                
                // Retrieve the student ID from StudentRecord
                StudentRecord sr = StudentRecord.findStudent(firstName, lastName);
                if (sr == null) {
                    System.out.println("Student not found for record: " + firstName + " " + lastName);
                    continue;
                }
                int studentId = sr.getId();

                // Pass the student ID to the GradeRecord constructor
                GradeRecord record = new GradeRecord(studentId, firstName, lastName, teacherAssigned);
                
                Map<Integer, String> teacherHistory = new HashMap<>();
                if (!teacherHistoryStr.isEmpty()) {
                    String[] entries = teacherHistoryStr.split(",");
                    for (String entry : entries) {
                        String[] parts = entry.split(":");
                        if (parts.length == 2) {
                            try {
                                int year = Integer.parseInt(parts[0]);
                                teacherHistory.put(year, parts[1]);
                            } catch(NumberFormatException e) { }
                        }
                    }
                }
                record.getTeacherHistory().clear();
                record.getTeacherHistory().putAll(teacherHistory);
                
                Map<String, Map<Integer, Integer>> grades = record.getAllGrades();
                String[] subjectEntries = gradesStr.split(";");
                for (String subjectEntry : subjectEntries) {
                    String[] subTokens = subjectEntry.split(":");
                    if (subTokens.length != 2) continue;
                    String subject = subTokens[0];
                    String[] termGrades = subTokens[1].split(",");
                    Map<Integer, Integer> termMap = new HashMap<>();
                    for (int term = 1; term <= 3 && term <= termGrades.length; term++) {
                        String gradeStr = termGrades[term - 1].trim();
                        try {
                            termMap.put(term, (gradeStr.isEmpty() || gradeStr.equalsIgnoreCase("N/A"))
                                    ? null : Integer.parseInt(gradeStr));
                        } catch (NumberFormatException e) {
                            termMap.put(term, null);
                        }
                    }
                    grades.put(subject, termMap);
                }
                gradeRecords.add(record);
                // Also update the corresponding student's individual file.
                if (sr != null) {
                    sr.updateIndividualFile();
                }
            }
            System.out.println("Loaded grade records from file.");
        } catch (IOException e) {
            System.out.println("Error loading grade records: " + e.getMessage());
        }
    }
    
    // Append a single record to the file.
    private static void appendRecordToFile(GradeRecord record) {
        File directory = new File("Data/Grades/");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(GRADE_RECORD_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(serializeRecord(record));
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing grade record: " + e.getMessage());
        }
    }
    
    public static void saveAllRecordsToFile() {
        File directory = new File("Data/Grades/");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(GRADE_RECORD_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (GradeRecord record : gradeRecords) {
                writer.write(serializeRecord(record));
                writer.newLine();
            }
            System.out.println("Grade records saved.");
        } catch (IOException e) {
            System.out.println("Error saving grade records: " + e.getMessage());
        }
    }
    
    // Serialize a GradeRecord to a string.
    private static String serializeRecord(GradeRecord record) {
        StringBuilder sb = new StringBuilder();
        sb.append(record.getStudentFirstName()).append("|")
          .append(record.getStudentLastName()).append("|")
          .append(record.getTeacherAssigned()).append("|");
        String teacherHistoryStr = record.getTeacherHistory().entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining(","));
        sb.append(teacherHistoryStr).append("|");
        String gradesStr = record.getAllGrades().entrySet().stream()
            .map(entry -> {
                String subject = entry.getKey();
                Map<Integer, Integer> termGrades = entry.getValue();
                String terms = "";
                for (int term = 1; term <= 3; term++) {
                    Integer grade = termGrades.get(term);
                    terms += (grade != null ? grade : "N/A");
                    if (term < 3) terms += ",";
                }
                return subject + ":" + terms;
            })
            .collect(Collectors.joining(";"));
        sb.append(gradesStr);
        return sb.toString();
    }
}
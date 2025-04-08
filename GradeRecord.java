package Data_Persistence;

import java.util.*;

public class GradeRecord {
    // List of subjects
    public static final List<String> SUBJECTS = Arrays.asList("Maths", "Language", "Phonics", "Art", "Spelling", "Music");

    private String studentFirstName;
    private String studentLastName;
    private String teacherAssigned;
    // Map to keep track of teacher history: key = year, value = teacher name
    private Map<Integer, String> teacherHistory;
    // Map of subject to term grades (term: 1, 2, 3)
    private Map<String, Map<Integer, Integer>> grades;
    private int currentYear;

    public GradeRecord(String studentFirstName, String studentLastName, String teacherAssigned) {
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.teacherAssigned = teacherAssigned;
        this.currentYear = Calendar.getInstance().get(Calendar.YEAR);
        
        // Initialize teacher history with the initial teacher assignment
        teacherHistory = new HashMap<>();
        teacherHistory.put(currentYear, teacherAssigned);
        
        // Initialize grades: for each subject, create a map for term grades (default to null)
        grades = new HashMap<>();
        for (String subject : SUBJECTS) {
            Map<Integer, Integer> termGrades = new HashMap<>();
            termGrades.put(1, null);
            termGrades.put(2, null);
            termGrades.put(3, null);
            grades.put(subject, termGrades);
        }
    }
    
    // Accessor to get a specific grade for a subject and term
    public Integer getGrade(String subject, int term) {
        if (grades.containsKey(subject)) {
            return grades.get(subject).get(term);
        }
        return null;
    }
    
    // Returns a table-like string of grades by term
    public String getGradesTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("Subject\tTerm1\tTerm2\tTerm3\n");
        for (String subject : SUBJECTS) {
            sb.append(subject).append("\t");
            for (int term = 1; term <= 3; term++) {
                Integer grade = grades.get(subject).get(term);
                sb.append((grade != null) ? grade : "N/A").append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    // Modifier: edit a grade for a subject and term
    public boolean editGrade(String subject, int term, int newGrade) {
        if (grades.containsKey(subject) && term >= 1 && term <= 3) {
            grades.get(subject).put(term, newGrade);
            return true;
        }
        return false;
    }
    
    // Modifier: remove (clear) a grade for a subject and term
    public boolean removeGrade(String subject, int term) {
        if (grades.containsKey(subject) && term >= 1 && term <= 3) {
            grades.get(subject).put(term, null);
            return true;
        }
        return false;
    }
    
    // Reassigns the student to a new teacher and records the assignment in the history.
    public void reassignTeacher(String newTeacher, int year) {
        this.teacherAssigned = newTeacher;
        teacherHistory.put(year, newTeacher);
    }
    
    // Generates a monthly report that includes the student's details and a grades table.
    public String generateMonthlyReport(String month, Integer year) {
        int reportYear = (year != null) ? year : currentYear;
        return String.format("Monthly Report for %s/%d for %s %s\nCurrent Teacher: %s\nTeacher History: %s\nGrades:\n%s",
                month, reportYear, studentFirstName, studentLastName,
                teacherAssigned, teacherHistory.toString(), getGradesTable());
    }
    
    // Getters for student and teacher info
    public String getStudentFirstName() {
        return studentFirstName;
    }
    
    public String getStudentLastName() {
        return studentLastName;
    }
    
    public String getTeacherAssigned() {
        return teacherAssigned;
    }
    
    public Map<Integer, String> getTeacherHistory() {
        return teacherHistory;
    }
    
    public Map<String, Map<Integer, Integer>> getAllGrades() {
        return grades;
    }
}


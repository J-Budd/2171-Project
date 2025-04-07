package Application_Logic;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class AttendanceManager {
    private static final String LAST_SAVED_DATE_FILE = "Data/Student/Attendance/LastSavedDate.txt";

    // Added comments for better readability
    // Validate attendance for all students
    public static boolean validateAttendance(DefaultTableModel model, int columnIndex) {
        for (int i = 0; i < model.getRowCount(); i++) {
            String value = (String) model.getValueAt(i, columnIndex);
            if (value == null || value.equals("None")) {
                JOptionPane.showMessageDialog(null, "All students must be marked Present or Absent.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    public static boolean isAttendanceAlreadySaved(String session) {
        try (Scanner scanner = new Scanner(new File(LAST_SAVED_DATE_FILE))) {
            if (!scanner.hasNextLine()) return false;
            String lastSavedDate = scanner.nextLine();
            if (!scanner.hasNextLine()) return false;
            String lastSavedSession = scanner.nextLine();
            String today = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            return lastSavedDate.equals(today) && lastSavedSession.equals(session);
        } catch (IOException e) {
            return false; // Assume no attendance saved if file doesn't exist
        }
    }

    public static void loadStudentNames(DefaultTableModel model) {
        File studentsListFile = new File("Data/Student/Records/StudentsList.txt");
        if (!studentsListFile.exists()) {
            JOptionPane.showMessageDialog(null, "StudentsList.txt not found. Please ensure student records are created.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Scanner scanner = new Scanner(studentsListFile)) {
            while (scanner.hasNextLine()) {
                String studentName = scanner.nextLine().trim();
                if (!studentName.isEmpty()) {
                    model.addRow(new Object[]{studentName, "None", "None", ""});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading student names: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void processAttendance(DefaultTableModel model, String session) {
        for (int i = 0; i < model.getRowCount(); i++) {
            String studentName = (String) model.getValueAt(i, 0);
            String attendance = session.equals("Morning") ? (String) model.getValueAt(i, 1) : (String) model.getValueAt(i, 2);
            String note = (String) model.getValueAt(i, 3);

            if (studentName != null && !studentName.trim().isEmpty() && attendance != null && !attendance.equals("None")) {
                System.out.println("Processing attendance for " + studentName + ": " + attendance + " (" + note + ")");
            }
        }
    }
}

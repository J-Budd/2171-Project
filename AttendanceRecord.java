package Data_Persistence;

import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AttendanceRecord {
    private static final String ATTENDANCE_DIR = "Data/Student/Attendance/";
    private static final String LAST_SAVED_DATE_FILE = ATTENDANCE_DIR + "LastSavedDate.txt";

    // Added comments for better readability
    // Save attendance records to file
    public static void saveAttendance(DefaultTableModel model, String session) {
        createAttendanceDirectory(ATTENDANCE_DIR); // Ensure directory exists

        String today = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String filename = ATTENDANCE_DIR + new SimpleDateFormat("MM").format(new Date()) + ".txt";

        createFileIfNotExists(filename); // Ensure attendance file exists

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            // Check if the date is already written in the file
            if (!isDateAlreadyWritten(today, filename)) {
                writer.write(today);
                writer.newLine();
            }

            writer.write(session + " Attendance");
            writer.newLine();
            for (int i = 0; i < model.getRowCount(); i++) {
                writer.write(model.getValueAt(i, 0) + "," + model.getValueAt(i, session.equals("Morning") ? 1 : 2) + "," + model.getValueAt(i, 3));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving attendance: " + e.getMessage());
        }

        saveLastSavedDate(today, session);
    }

    private static boolean isDateAlreadyWritten(String date, String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(date)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error checking date in file: " + e.getMessage());
        }
        return false;
    }

    private static void saveLastSavedDate(String date, String session) {
        createAttendanceDirectory(ATTENDANCE_DIR); // Ensure directory exists
        createFileIfNotExists(LAST_SAVED_DATE_FILE); // Ensure saved date file exists

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LAST_SAVED_DATE_FILE, false))) {
            writer.write(date);
            writer.newLine();
            writer.write(session);
        } catch (IOException e) {
            System.out.println("Error saving last saved date: " + e.getMessage());
        }
    }

    public static void loadAttendance(DefaultTableModel model) {
        createAttendanceDirectory(ATTENDANCE_DIR); // Ensure directory exists

        String filename = ATTENDANCE_DIR + new SimpleDateFormat("MM").format(new Date()) + ".txt";
        createFileIfNotExists(filename); // Ensure attendance file exists

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isMorning = true;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Attendance")) {
                    isMorning = line.contains("Morning");
                    continue;
                }

                String[] data = line.split(",", -1);
                if (data.length >= 3) {
                    String studentName = data[0].trim();
                    String attendance = data[1].trim();
                    String note = data[2].trim();

                    for (int i = 0; i < model.getRowCount(); i++) {
                        if (model.getValueAt(i, 0).equals(studentName)) {
                            if (isMorning) {
                                model.setValueAt(attendance, i, 1);
                            } else {
                                model.setValueAt(attendance, i, 2);
                            }
                            model.setValueAt(note, i, 3);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading attendance: " + e.getMessage());
        }
    }

    private static void createAttendanceDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Attendance directory created: " + directoryPath);
            } else {
                System.out.println("Failed to create attendance directory: " + directoryPath);
            }
        }
    }

    private static void createFileIfNotExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("File created: " + filePath);
                }
            } catch (IOException e) {
                System.out.println("Error creating file: " + filePath + " - " + e.getMessage());
            }
        }
    }
}

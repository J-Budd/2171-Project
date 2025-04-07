package UI;

import Application_Logic.AttendanceManager;
import Data_Persistence.AttendanceRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class AttendanceListingUI extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnSaveMorning;
    private JButton btnSaveAfternoon;
    private JComboBox<String> attendMornTypes;
    private JComboBox<String> attendNoonTypes;

    public AttendanceListingUI() {
        setTitle("Attendance Listing");
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1000, 600));

        // Initialize buttons first
        btnSaveMorning = new JButton("Save Morning Attendance");
        btnSaveAfternoon = new JButton("Save Afternoon Attendance");

        // Table Setup
        String[] columnNames = {"Full Name", "Morning", "Afternoon", "Note"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);

        // Morning Attendance Column
        TableColumn morningColumn = table.getColumnModel().getColumn(1);
        attendMornTypes = new JComboBox<>();
        attendMornTypes.addItem("None");
        attendMornTypes.addItem("Present");
        attendMornTypes.addItem("Absent");
        morningColumn.setCellEditor(new DefaultCellEditor(attendMornTypes));

        // Afternoon Attendance Column
        TableColumn afternoonColumn = table.getColumnModel().getColumn(2);
        attendNoonTypes = new JComboBox<>();
        attendNoonTypes.addItem("None");
        attendNoonTypes.addItem("Present");
        attendNoonTypes.addItem("Absent");
        afternoonColumn.setCellEditor(new DefaultCellEditor(attendNoonTypes));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Load Student Names and Attendance
        AttendanceManager.loadStudentNames(model);
        AttendanceRecord.loadAttendance(model);

        // Initialize attendance state
        initializeAttendanceState();

        // Button Panel
        JPanel pnlButtons = new JPanel();
        pnlButtons.add(btnSaveMorning);
        pnlButtons.add(btnSaveAfternoon);
        add(pnlButtons, BorderLayout.SOUTH);

        // Button Actions
        btnSaveMorning.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveMorningAttendance();
            }
        });

        btnSaveAfternoon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAfternoonAttendance();
            }
        });

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initializeAttendanceState() {
        String savedDate = "";
        String savedSession = "";

        try (Scanner scanner = new Scanner(new File("Data/Student/Attendance/LastSavedDate.txt"))) {
            if (scanner.hasNextLine()) {
                savedDate = scanner.nextLine();
            }
            if (scanner.hasNextLine()) {
                savedSession = scanner.nextLine();
            }
        } catch (IOException e) {
            System.out.println("Error reading LastSavedDate.txt: " + e.getMessage());
        }

        String today = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        if (!savedDate.equals(today)) {
            enableAllControls();
        } else {
            if (savedSession.contains("Morning")) {
                disableMorningControls();
            }
            if (savedSession.contains("Afternoon")) {
                disableMorningControls();
                disableAfternoonControls();
            }
        }
    }

    private void enableAllControls() {
        btnSaveMorning.setEnabled(true);
        btnSaveAfternoon.setEnabled(true);
        attendMornTypes.setEnabled(true);
        attendNoonTypes.setEnabled(true);
    }

    private void disableMorningControls() {
        btnSaveMorning.setEnabled(false);
        attendMornTypes.setEnabled(false);
    }

    private void disableAfternoonControls() {
        btnSaveAfternoon.setEnabled(false);
        attendNoonTypes.setEnabled(false);
    }

    private void saveMorningAttendance() {
        if (!validateAttendance(1)) {
            JOptionPane.showMessageDialog(this, "All students must be marked Present or Absent for Morning Attendance.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        saveAttendanceToFile("Morning");
        disableMorningControls();
    }

    private void saveAfternoonAttendance() {
        if (btnSaveMorning.isEnabled()) {
            JOptionPane.showMessageDialog(this, "Morning Attendance must be saved before Afternoon Attendance.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!validateAttendance(2)) {
            JOptionPane.showMessageDialog(this, "All students must be marked Present or Absent for Afternoon Attendance.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        saveAttendanceToFile("Afternoon");
        disableAfternoonControls();
    }

    private boolean validateAttendance(int columnIndex) {
        for (int i = 0; i < table.getRowCount(); i++) {
            String value = (String) table.getValueAt(i, columnIndex);
            if (value == null || value.equals("None")) {
                return false;
            }
        }
        return true;
    }

    private void saveAttendanceToFile(String session) {
        String today = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String filename = "Data/Student/Attendance/" + new SimpleDateFormat("MM").format(new Date()) + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(today);
            writer.newLine();
            writer.write(session + " Attendance");
            writer.newLine();
            for (int i = 0; i < table.getRowCount(); i++) {
                writer.write(table.getValueAt(i, 0) + "," + table.getValueAt(i, session.equals("Morning") ? 1 : 2) + "," + table.getValueAt(i, 3));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving attendance: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Data/Student/Attendance/LastSavedDate.txt"))) {
            writer.write(today);
            writer.newLine();
            writer.write(session);
        } catch (IOException e) {
            System.out.println("Error updating LastSavedDate.txt: " + e.getMessage());
        }
    }
}

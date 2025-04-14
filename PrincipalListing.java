package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

import Application_Logic.StudentManager;
import Application_Logic.GradeManager;
import Application_Logic.AttendanceManager;
import Application_Logic.ExpenseManager;
import Data_Persistence.StudentRecord;
import Data_Persistence.GradeRecord;
import Data_Persistence.AttendanceRecord;
import UI.ExpenseGUI;
import UI.ExpenseDialog;
import UI.AttendanceScreen;

public class PrincipalListing extends JFrame {
    private JButton btnViewAttendance;
    private JButton btnViewGrades;
    private JButton btnViewExpenses;
    private JButton btnViewStudentRecords;
    private JButton btnClose;

    public PrincipalListing() {
        setTitle("Principal Dashboard");
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1000, 100));
        setBackground(Color.decode("#A3BFDD"));

        // Greeting Panel
        JPanel pnlGreeting = new JPanel();
        pnlGreeting.setBackground(Color.decode("#A3BFDD"));
        pnlGreeting.setForeground(Color.decode("#A3BFDD")); 
        JLabel lblGreeting = new JLabel("Welcome to the Principal Dashboard");
        lblGreeting.setForeground(Color.decode("#191919"));
        lblGreeting.setFont(new Font("Arial", Font.BOLD, 24));
        pnlGreeting.add(lblGreeting);
        add(pnlGreeting, BorderLayout.NORTH);

        // Command Panel
        JPanel pnlCommand = new JPanel();
        pnlCommand.setBackground(Color.decode("#A3BFDD"));
        pnlCommand.setForeground(Color.decode("#A3BFDD")); 
        btnViewAttendance = new JButton("View Attendance");
        btnViewGrades = new JButton("View Grades");
        btnViewExpenses = new JButton("View Expenses");
        btnViewStudentRecords = new JButton("View Students");
        btnClose = new JButton("Close");

        btnViewAttendance.setBackground(Color.decode("#A31621")); 
        btnViewAttendance.setForeground(Color.WHITE); 
        btnViewAttendance.setBorder(null);
        btnViewAttendance.setPreferredSize(new Dimension(100, 20));
        btnViewGrades.setBackground(Color.decode("#A31621")); 
        btnViewGrades.setForeground(Color.WHITE); 
        btnViewGrades.setBorder(null);
        btnViewGrades.setPreferredSize(new Dimension(100, 20));
        btnViewExpenses.setBackground(Color.decode("#A31621")); 
        btnViewExpenses.setForeground(Color.WHITE); 
        btnViewExpenses.setBorder(null);
        btnViewExpenses.setPreferredSize(new Dimension(100, 20));
        btnViewStudentRecords.setBackground(Color.decode("#A31621")); 
        btnViewStudentRecords.setForeground(Color.WHITE); 
        btnViewStudentRecords.setBorder(null);
        btnViewStudentRecords.setPreferredSize(new Dimension(100, 20));
        btnClose.setBackground(Color.decode("#A31621")); 
        btnClose.setForeground(Color.WHITE); 
        btnClose.setBorder(null);
        btnClose.setPreferredSize(new Dimension(100, 20));

        pnlCommand.add(btnViewAttendance);
        pnlCommand.add(btnViewGrades);
        pnlCommand.add(btnViewExpenses);
        pnlCommand.add(btnViewStudentRecords);
        pnlCommand.add(btnClose);
        add(pnlCommand, BorderLayout.SOUTH);

        // Button Actions
        btnViewAttendance.addActionListener(e -> new AttendanceScreen().setVisible(true));
        btnViewStudentRecords.addActionListener(e -> new StudentRecordsWindow().setVisible(true));
        btnViewGrades.addActionListener(e -> new GradeUI().setVisible(true));
        btnViewExpenses.addActionListener(e -> new ExpenseGUI().setVisible(true));
        btnClose.addActionListener(e -> System.exit(0));

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // ===============================
    // StudentRecordsWindow (recent version)
    // ===============================
    private class StudentRecordsWindow extends JFrame {
        private DefaultTableModel model;
        private JTable table;
        private JButton cmdAddStudent;
        private JButton cmdUpdateStudent;
        private JButton cmdDeleteStudent;
        private JButton cmdSearch;

        public StudentRecordsWindow() {
            setTitle("Student Records");
            setLayout(new BorderLayout());
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setPreferredSize(new Dimension(1000,700));

            JPanel pnlCommand = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JPanel pnlDisplay = new JPanel(new BorderLayout());

            cmdAddStudent = new JButton("Add Student Record");
            cmdAddStudent.setBackground(Color.decode("#A31621"));
            cmdAddStudent.setForeground(Color.WHITE);
            cmdAddStudent.setBorder(null);

            cmdUpdateStudent = new JButton("Update Student Record");
            cmdUpdateStudent.setForeground(Color.WHITE);
            cmdUpdateStudent.setBorder(null);
            cmdUpdateStudent.setBackground(Color.decode("#A31621"));

            cmdDeleteStudent = new JButton("Delete Student Record");
            cmdDeleteStudent.setBackground(Color.decode("#A31621"));
            cmdDeleteStudent.setForeground(Color.WHITE);
            cmdDeleteStudent.setBorder(null);

            cmdSearch = new JButton("Search Student Record");
            cmdSearch.setBackground(Color.decode("#A31621"));
            cmdSearch.setForeground(Color.WHITE);
            cmdSearch.setBorder(null);

            cmdSearch.addActionListener(e -> new SearchStudentsWindow().setVisible(true));

            cmdAddStudent.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new AddUpdateStudentWindow(null, -1).setVisible(true);
                }
            });

            cmdUpdateStudent.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        // Use 7 fields as in her version
                        String[] studentData = new String[7];
                        for (int i = 0; i < 7; i++) {
                            studentData[i] = table.getValueAt(selectedRow, i).toString();
                        }
                        new AddUpdateStudentWindow(studentData, selectedRow).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(StudentRecordsWindow.this, "Please select a student to update.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            cmdDeleteStudent.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        String firstName = table.getValueAt(selectedRow, 0).toString();
                        String lastName = table.getValueAt(selectedRow, 1).toString();
                        StudentRecord sr = StudentRecord.findStudent(firstName, lastName);
                        if (sr != null) {
                            int confirm = JOptionPane.showConfirmDialog(
                                StudentRecordsWindow.this, 
                                "Are you sure you want to delete " + firstName + " " + lastName + "'s record?",
                                "Confirm Deletion", 
                                JOptionPane.YES_NO_OPTION
                            );
                            if (confirm == JOptionPane.YES_OPTION) {
                                sr.deleteStudent();
                                refreshTable();
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(StudentRecordsWindow.this, 
                                                      "Please select a student record to delete.", 
                                                      "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            pnlCommand.add(cmdAddStudent);
            pnlCommand.add(cmdUpdateStudent);
            pnlCommand.add(cmdDeleteStudent);
            pnlCommand.add(cmdSearch);
            pnlCommand.setBackground(Color.decode("#A3BFDD"));

            String[] columnNames = {"First Name", "Last Name", "Birth Date", "Address", "Guardian", "Regular Contact", "Emergency Contact"};
            model = new DefaultTableModel(columnNames, 0);
            table = new JTable(model);
            table.setBackground(Color.decode("#A3BFDD"));

            pnlDisplay.add(new JScrollPane(table), BorderLayout.CENTER);

            add(pnlDisplay, BorderLayout.CENTER);
            add(pnlCommand, BorderLayout.SOUTH);

            refreshTable();

            pack();
            setLocationRelativeTo(null);
        }

        private void refreshTable() {
            updateTable(StudentRecord.studentList);
        }
        
        // Method to update the table with a given list of student records
        private void updateTable(List<StudentRecord> records) {
            model.setRowCount(0);
            StudentManager.initialize(); // Ensure the list is loaded/updated
            for (StudentRecord sr : records) {
                model.addRow(new Object[]{
                    sr.getFirstName(),
                    sr.getLastName(),
                    sr.getBirthdate(),
                    sr.getAddress(),
                    sr.getGuardian(),
                    sr.getRegularContact(),
                    sr.getEmergencyContact()
                });
            }
        }

        private class AddUpdateStudentWindow extends JFrame {
            private JTextField[] textFields;
            private JButton cmdSave;
            private JButton cmdCancel;
            private int selectedRow;

            public AddUpdateStudentWindow(String[] studentData, int selectedRow) {
                this.selectedRow = selectedRow;
                setTitle(studentData == null ? "Add Student Record" : "Update Student Record");
                setLayout(new GridLayout(8, 2, 10, 10));
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                String[] labels = {"First Name:", "Last Name:", "Birth Date:", "Address:", "Guardian:", "Regular Contact:", "Emergency Contact:"};
                textFields = new JTextField[7];
                for (int i = 0; i < 7; i++) {
                    add(new JLabel(labels[i]));
                    textFields[i] = new JTextField();
                    if (studentData != null) {
                        textFields[i].setText(studentData[i]);
                    }
                    add(textFields[i]);
                }

                cmdSave = new JButton("Save");
                cmdCancel = new JButton("Cancel");

                cmdSave.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        for (JTextField textField : textFields) {
                            if (textField.getText().trim().isEmpty()) {
                                JOptionPane.showMessageDialog(AddUpdateStudentWindow.this, "All fields must be filled in.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                        int result = JOptionPane.showConfirmDialog(AddUpdateStudentWindow.this, "Are you sure you want to save?", "Confirmation", JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.YES_OPTION) {
                            if (studentData == null) {
                                StudentManager.addStudent(
                                    textFields[0].getText(),
                                    textFields[1].getText(),
                                    textFields[2].getText(),
                                    textFields[3].getText(),
                                    textFields[4].getText(),
                                    textFields[5].getText(),
                                    textFields[6].getText()
                                );
                            } else {
                                String oldFirstName = table.getValueAt(selectedRow, 0).toString();
                                String oldLastName = table.getValueAt(selectedRow, 1).toString();
                                StudentManager.updateStudent(
                                    oldFirstName,
                                    oldLastName,
                                    textFields[0].getText(),
                                    textFields[1].getText(),
                                    textFields[2].getText(),
                                    textFields[3].getText(),
                                    textFields[4].getText(),
                                    textFields[5].getText(),
                                    textFields[6].getText()
                                );
                            }
                            refreshTable();
                            dispose();
                        }
                    }
                });

                cmdCancel.addActionListener(e -> dispose());

                add(cmdSave);
                add(cmdCancel);

                pack();
                setLocationRelativeTo(null);
            }
        }

        private class SearchStudentsWindow extends JFrame {
            private JComboBox<String> searchCriteria;
            private JTextField searchField;
            private JButton searchButton;
            private JButton clearSearchButton;

            public SearchStudentsWindow() {
                setTitle("Search Students");
                setLayout(new BorderLayout());
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                setPreferredSize(new Dimension(400, 200));

                JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                searchPanel.add(new JLabel("Search by:"));
                String[] options = {"ID", "First Name", "Last Name"};
                searchCriteria = new JComboBox<>(options);
                searchPanel.add(searchCriteria);
                searchField = new JTextField(15);
                searchPanel.add(searchField);
                searchButton = new JButton("Search");
                searchPanel.add(searchButton);
                clearSearchButton = new JButton("Clear");
                searchPanel.add(clearSearchButton);

                add(searchPanel, BorderLayout.CENTER);

                searchButton.addActionListener(e -> {
                    String criteria = (String) searchCriteria.getSelectedItem();
                    String searchText = searchField.getText().trim();
                    if (searchText.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Search field cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    List<StudentRecord> filtered = new ArrayList<>();
                    if (criteria.equals("ID")) {
                        try {
                            int id = Integer.parseInt(searchText);
                            for (StudentRecord sr : StudentRecord.studentList) {
                                if (sr.getId() == id)
                                    filtered.add(sr);
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (criteria.equals("First Name")) {
                        for (StudentRecord sr : StudentRecord.studentList) {
                            if (sr.getFirstName().equalsIgnoreCase(searchText))
                                filtered.add(sr);
                        }
                    } else if (criteria.equals("Last Name")) {
                        for (StudentRecord sr : StudentRecord.studentList) {
                            if (sr.getLastName().equalsIgnoreCase(searchText))
                                filtered.add(sr);
                        }
                    }
                    updateTable(filtered);
                });

                clearSearchButton.addActionListener(e -> {
                    searchField.setText("");
                    refreshTable();
                });

                pack();
                setLocationRelativeTo(null);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PrincipalListing().setVisible(true));
    }
}

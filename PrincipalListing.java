package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
        setBackground(Color.decode("#A3BFDD")); // Set background color

        // Greeting Panel
        JPanel pnlGreeting = new JPanel();
        pnlGreeting.setBackground(Color.decode("#A3BFDD")); // Set background color
        pnlGreeting.setForeground(Color.decode("#A3BFDD")); // Set foreground color
        JLabel lblGreeting = new JLabel("Welcome to the Principal Dashboard");
        lblGreeting.setForeground(Color.decode("#191919")); // Set text color
        lblGreeting.setFont(new Font("Arial", Font.BOLD, 24)); // Set title font
        pnlGreeting.add(lblGreeting);
        add(pnlGreeting, BorderLayout.NORTH);

        // Command Panel
        JPanel pnlCommand = new JPanel();
        pnlCommand.setBackground(Color.decode("#A3BFDD")); // Set background color
        pnlCommand.setForeground(Color.decode("#A3BFDD")); // Set foreground color
        btnViewAttendance = new JButton("View Attendance");
        btnViewGrades = new JButton("View Grades");
        btnViewExpenses = new JButton("View Expenses");
        btnViewStudentRecords = new JButton("View Students");
        btnClose = new JButton("Close");

        btnViewAttendance.setBackground(Color.decode("#A31621")); // Set button background color
        btnViewAttendance.setForeground(Color.WHITE); // Set button text color
        btnViewAttendance.setBorder(null);
        btnViewAttendance.setPreferredSize(new Dimension(100, 20));
        btnViewGrades.setBackground(Color.decode("#A31621")); // Set button background color
        btnViewGrades.setForeground(Color.WHITE); // Set button text color
        btnViewGrades.setBorder(null);
        btnViewGrades.setPreferredSize(new Dimension(100, 20));
        btnViewExpenses.setBackground(Color.decode("#A31621")); // Set button background color
        btnViewExpenses.setForeground(Color.WHITE); // Set button text color
        btnViewExpenses.setBorder(null);
        btnViewExpenses.setPreferredSize(new Dimension(100, 20));
        btnViewStudentRecords.setBackground(Color.decode("#A31621")); // Set button background color
        btnViewStudentRecords.setForeground(Color.WHITE); // Set button text color
        btnViewStudentRecords.setBorder(null);
        btnViewStudentRecords.setPreferredSize(new Dimension(100, 20));
        btnClose.setBackground(Color.decode("#A31621")); // Set button background color
        btnClose.setForeground(Color.WHITE); // Set button text color
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
        private JButton cmdSortByLastName;
        private JButton cmdSortByBirthyear;

        public StudentRecordsWindow() {
            setTitle("Student Records");
            setLayout(new BorderLayout());
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel pnlCommand = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JPanel pnlDisplay = new JPanel(new BorderLayout());

            cmdAddStudent = new JButton("Add Student Record");
            cmdUpdateStudent = new JButton("Update Student Record");
            cmdDeleteStudent = new JButton("Delete Student Record");
            cmdSortByLastName = new JButton("Sort by Last Name");
            cmdSortByBirthyear = new JButton("Sort by Birthyear");

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

            /* The delete and sort actions are currently commented out in her version.
               They can be re-enabled if needed. */
            pnlCommand.add(cmdAddStudent);
            pnlCommand.add(cmdUpdateStudent);
            pnlCommand.add(cmdDeleteStudent);
            pnlCommand.add(cmdSortByLastName);
            pnlCommand.add(cmdSortByBirthyear);

            String[] columnNames = {"First Name", "Last Name", "Birth Date", "Address", "Guardian", "Regular Contact", "Emergency Contact"};
            model = new DefaultTableModel(columnNames, 0);
            table = new JTable(model);

            pnlDisplay.add(new JScrollPane(table), BorderLayout.CENTER);

            add(pnlDisplay, BorderLayout.CENTER);
            add(pnlCommand, BorderLayout.SOUTH);

            refreshTable();

            pack();
            setLocationRelativeTo(null);
        }

        private void refreshTable() {
            model.setRowCount(0);
            StudentManager.initialize();
            List<StudentRecord> students = StudentRecord.studentList;
            for (StudentRecord sr : students) {
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
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PrincipalListing().setVisible(true));
    }
}

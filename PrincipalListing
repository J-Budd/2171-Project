package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Comparator;

import Data_Persistence.StudentRecord;
import Application_Logic.StudentManager;
import Data_Persistence.GradeRecord;
import Data_Persistence.GradeRecord.GradeEntry;
import Application_Logic.GradeManager;

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

        // Greeting Panel
        JPanel pnlGreeting = new JPanel();
        JLabel lblGreeting = new JLabel("Welcome to the Principal Dashboard");
        pnlGreeting.add(lblGreeting);
        add(pnlGreeting, BorderLayout.NORTH);

        // Command Panel
        JPanel pnlCommand = new JPanel();
        btnViewAttendance = new JButton("View Attendance");
        btnViewGrades = new JButton("View Grades");
        btnViewExpenses = new JButton("View Expenses");
        btnViewStudentRecords = new JButton("View Student Records");
        btnClose = new JButton("Close");

        pnlCommand.add(btnViewAttendance);
        pnlCommand.add(btnViewGrades);
        pnlCommand.add(btnViewExpenses);
        pnlCommand.add(btnViewStudentRecords);
        pnlCommand.add(btnClose);
        add(pnlCommand, BorderLayout.SOUTH);

        // Button Actions
        btnViewAttendance.addActionListener(e -> new AttendanceScreen().setVisible(true));
        btnViewStudentRecords.addActionListener(e -> new StudentRecordsWindow().setVisible(true));
        btnViewGrades.addActionListener(e -> new GradeRecordsWindow().setVisible(true));
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

    // ===============================
    // GradeRecordsWindow (enhanced grade entry)
    // ===============================
    private class GradeRecordsWindow extends JFrame {
        private DefaultTableModel model;
        private JTable table;
        private JButton cmdAddGradeRecord;
        private JButton cmdUpdateGradeRecord;
        private JButton cmdDeleteGradeRecord;
        private JButton cmdViewGradeDetails;
        private JButton cmdSortByStudent;
        private JButton cmdSortByTeacher;

        public GradeRecordsWindow() {
            setTitle("Grade Records");
            setLayout(new BorderLayout());
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel pnlCommand = new JPanel(new FlowLayout(FlowLayout.CENTER));
            cmdAddGradeRecord = new JButton("Add Grade Record");
            cmdUpdateGradeRecord = new JButton("Update Grade Record");
            cmdDeleteGradeRecord = new JButton("Delete Grade Record");
            cmdViewGradeDetails = new JButton("View Grade Details");
            cmdSortByStudent = new JButton("Sort by Student");
            cmdSortByTeacher = new JButton("Sort by Teacher");

            pnlCommand.add(cmdAddGradeRecord);
            pnlCommand.add(cmdUpdateGradeRecord);
            pnlCommand.add(cmdDeleteGradeRecord);
            pnlCommand.add(cmdViewGradeDetails);
            pnlCommand.add(cmdSortByStudent);
            pnlCommand.add(cmdSortByTeacher);
            add(pnlCommand, BorderLayout.SOUTH);

            // Columns now include a Student ID column for clarity. If not available, "N/A" is shown.
            String[] columnNames = {"Student ID", "First Name", "Last Name", "Teacher", "Monthly Report"};
            model = new DefaultTableModel(columnNames, 0);
            table = new JTable(model);
            add(new JScrollPane(table), BorderLayout.CENTER);

            cmdAddGradeRecord.addActionListener(e -> new AddUpdateGradeWindow(null, -1).setVisible(true));

            cmdUpdateGradeRecord.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String[] gradeData = new String[4];
                    for (int i = 0; i < 4; i++) {
                        gradeData[i] = table.getValueAt(selectedRow, i).toString();
                    }
                    new AddUpdateGradeWindow(gradeData, selectedRow).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(GradeRecordsWindow.this, "Please select a grade record to update.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            cmdDeleteGradeRecord.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String firstName = table.getValueAt(selectedRow, 1).toString();
                    String lastName = table.getValueAt(selectedRow, 2).toString();
                    GradeRecord record = GradeManager.findGradeRecord(firstName, lastName);
                    if (record != null) {
                        GradeManager.gradeRecords.remove(record);
                        GradeManager.saveAllRecordsToFile();
                        refreshTable();
                    }
                } else {
                    JOptionPane.showMessageDialog(GradeRecordsWindow.this, "Please select a grade record to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            cmdViewGradeDetails.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String firstName = table.getValueAt(selectedRow, 1).toString();
                    String lastName = table.getValueAt(selectedRow, 2).toString();
                    GradeRecord record = GradeManager.findGradeRecord(firstName, lastName);
                    if (record != null) {
                        JOptionPane.showMessageDialog(GradeRecordsWindow.this, record.getMonthlyReport(), "Grade Details", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(GradeRecordsWindow.this, "Please select a grade record to view details.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            cmdSortByStudent.addActionListener(e -> {
                List<GradeRecord> records = GradeManager.gradeRecords;
                records.sort(Comparator.comparing(GradeRecord::getStudentLastName));
                refreshTable(records);
            });

            cmdSortByTeacher.addActionListener(e -> {
                List<GradeRecord> records = GradeManager.gradeRecords;
                records.sort(Comparator.comparing(GradeRecord::getTeacherAssigned));
                refreshTable(records);
            });

            refreshTable();
            pack();
            setLocationRelativeTo(null);
        }

        private void refreshTable() {
            refreshTable(GradeManager.gradeRecords);
        }

        private void refreshTable(List<GradeRecord> records) {
            model.setRowCount(0);
            for (GradeRecord gr : records) {
                // Student ID is retrieved from StudentRecord if available; otherwise, show "N/A"
                String studentId = "N/A";
                StudentRecord sr = StudentRecord.findStudent(gr.getStudentFirstName(), gr.getStudentLastName());
                if (sr != null) {
                    studentId = String.valueOf(sr.getId());
                }
                model.addRow(new Object[]{
                    studentId,
                    gr.getStudentFirstName(),
                    gr.getStudentLastName(),
                    gr.getTeacherAssigned(),
                    gr.getMonthlyReport()
                });
            }
        }

        // -------------------------------
        // AddUpdateGradeWindow (enhanced dialog)
        // -------------------------------
        private class AddUpdateGradeWindow extends JFrame {
            private JTextField txtStudentID;
            private JTextField txtStudentFirstName;
            private JTextField txtStudentLastName;
            private JTextField txtTeacherAssigned;
            private JComboBox<String> cmbSubject;
            private JTextField txtGrade;
            private JTextField txtDate;
            private JButton btnAddEntry;
            private JButton btnRemoveEntry;
            private DefaultTableModel gradeTableModel;
            private JTable gradeTable;
            private JButton cmdSave;
            private JButton cmdCancel;
            private int selectedRow;
            private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            public AddUpdateGradeWindow(String[] gradeData, int selectedRow) {
                this.selectedRow = selectedRow;
                setTitle(gradeData == null ? "Add Grade Record" : "Update Grade Record");
                setLayout(new BorderLayout(10, 10));
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                // Top Panel: Student Info (ID is read-only)
                JPanel pnlStudentInfo = new JPanel(new GridLayout(2, 4, 10, 10));
                pnlStudentInfo.add(new JLabel("Student ID:"));
                txtStudentID = new JTextField();
                txtStudentID.setEditable(false);
                pnlStudentInfo.add(txtStudentID);
                pnlStudentInfo.add(new JLabel("First Name:"));
                txtStudentFirstName = new JTextField();
                pnlStudentInfo.add(txtStudentFirstName);
                pnlStudentInfo.add(new JLabel("Last Name:"));
                txtStudentLastName = new JTextField();
                pnlStudentInfo.add(txtStudentLastName);
                pnlStudentInfo.add(new JLabel("Teacher Assigned:"));
                txtTeacherAssigned = new JTextField();
                pnlStudentInfo.add(txtTeacherAssigned);
                add(pnlStudentInfo, BorderLayout.NORTH);

                // Center Panel: Grade Entry Section
                JPanel pnlEntry = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
                cmbSubject = new JComboBox<>(GradeRecord.SUBJECTS.toArray(new String[0]));
                pnlEntry.add(new JLabel("Subject:"));
                pnlEntry.add(cmbSubject);
                txtGrade = new JTextField(5);
                pnlEntry.add(new JLabel("Grade:"));
                pnlEntry.add(txtGrade);
                txtDate = new JTextField(10);
                txtDate.setText(LocalDate.now().format(dtFormatter));
                pnlEntry.add(new JLabel("Date (yyyy-MM-dd):"));
                pnlEntry.add(txtDate);
                btnAddEntry = new JButton("Add Entry");
                pnlEntry.add(btnAddEntry);
                btnRemoveEntry = new JButton("Remove Selected");
                pnlEntry.add(btnRemoveEntry);
                add(pnlEntry, BorderLayout.CENTER);

                // Table for grade entries
                String[] columns = {"Subject", "Grade", "Date"};
                gradeTableModel = new DefaultTableModel(columns, 0);
                gradeTable = new JTable(gradeTableModel);
                add(new JScrollPane(gradeTable), BorderLayout.SOUTH);

                // Bottom Panel: Save/Cancel
                JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                cmdSave = new JButton("Save");
                cmdCancel = new JButton("Cancel");
                pnlButtons.add(cmdSave);
                pnlButtons.add(cmdCancel);
                add(pnlButtons, BorderLayout.PAGE_END);

                // Button Actions for grade entry
                btnAddEntry.addActionListener(e -> {
                    String subject = (String) cmbSubject.getSelectedItem();
                    String gradeStr = txtGrade.getText().trim();
                    String dateStr = txtDate.getText().trim();
                    if (gradeStr.isEmpty() || dateStr.isEmpty()) {
                        JOptionPane.showMessageDialog(AddUpdateGradeWindow.this, "Grade and Date must be provided.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try {
                        int grade = Integer.parseInt(gradeStr);
                        LocalDate date = LocalDate.parse(dateStr, dtFormatter);
                        gradeTableModel.addRow(new Object[]{subject, grade, date.format(dtFormatter)});
                        txtGrade.setText("");
                        txtDate.setText(LocalDate.now().format(dtFormatter));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(AddUpdateGradeWindow.this, "Invalid grade or date format.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                
                btnRemoveEntry.addActionListener(e -> {
                    int selected = gradeTable.getSelectedRow();
                    if (selected != -1) {
                        gradeTableModel.removeRow(selected);
                    }
                });

                cmdSave.addActionListener(e -> {
                    if (txtStudentFirstName.getText().trim().isEmpty() ||
                        txtStudentLastName.getText().trim().isEmpty() ||
                        txtTeacherAssigned.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(AddUpdateGradeWindow.this, "Student and teacher fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (gradeTableModel.getRowCount() == 0) {
                        int confirm = JOptionPane.showConfirmDialog(AddUpdateGradeWindow.this, "No grade entries added. Continue?", "Confirmation", JOptionPane.YES_NO_OPTION);
                        if (confirm != JOptionPane.YES_OPTION) return;
                    }
                    // Retrieve or create a GradeRecord
                    GradeRecord record = GradeManager.findGradeRecord(txtStudentFirstName.getText(), txtStudentLastName.getText());
                    if (record == null) {
                        record = new GradeRecord(
                            txtStudentFirstName.getText(),
                            txtStudentLastName.getText(),
                            txtTeacherAssigned.getText()
                        );
                    } else {
                        // Update teacher assignment and clear existing grade entries
                        record.reassignTeacher(txtTeacherAssigned.getText(), LocalDate.now().getYear());
                        for (String subj : GradeRecord.SUBJECTS) {
                            record.getAllGrades().get(subj).clear();
                        }
                    }
                    // Add grade entries from the table
                    for (int i = 0; i < gradeTableModel.getRowCount(); i++) {
                        String subj = gradeTableModel.getValueAt(i, 0).toString();
                        int grd = Integer.parseInt(gradeTableModel.getValueAt(i, 1).toString());
                        LocalDate dt = LocalDate.parse(gradeTableModel.getValueAt(i, 2).toString(), dtFormatter);
                        record.addGrade(subj, grd, dt);
                    }
                    if (GradeManager.findGradeRecord(txtStudentFirstName.getText(), txtStudentLastName.getText()) == null) {
                        GradeManager.addGradeRecord(record);
                    } else {
                        GradeManager.saveAllRecordsToFile();
                        StudentRecord sr = StudentRecord.findStudent(txtStudentFirstName.getText(), txtStudentLastName.getText());
                        if (sr != null) sr.updateIndividualFile();
                    }
                    refreshTable();
                    dispose();
                });

                cmdCancel.addActionListener(e -> dispose());

                // Pre-fill fields if updating
                if (gradeData != null) {
                    GradeRecord existingRecord = GradeManager.findGradeRecord(gradeData[0], gradeData[1]);
                    if (existingRecord != null) {
                        txtStudentFirstName.setText(existingRecord.getStudentFirstName());
                        txtStudentLastName.setText(existingRecord.getStudentLastName());
                        txtTeacherAssigned.setText(existingRecord.getTeacherAssigned());
                        StudentRecord sr = StudentRecord.findStudent(existingRecord.getStudentFirstName(), existingRecord.getStudentLastName());
                        if (sr != null) {
                            txtStudentID.setText(String.valueOf(sr.getId()));
                        }
                        for (String subj : GradeRecord.SUBJECTS) {
                            for (GradeEntry ge : existingRecord.getAllGrades().get(subj)) {
                                gradeTableModel.addRow(new Object[]{subj, ge.getGrade(), ge.getDate().format(dtFormatter)});
                            }
                        }
                    }
                } else {
                    txtStudentID.setText("Auto-generated");
                }

                pack();
                setLocationRelativeTo(null);
            }
        }
    }

    // -------------------------------
    // AttendanceScreen stub (unchanged)
    // -------------------------------
    private class AttendanceScreen extends JFrame {
        public AttendanceScreen() {
            setTitle("Attendance Screen");
            setSize(400, 300);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            add(new JLabel("Attendance functionality goes here."), BorderLayout.CENTER);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PrincipalListing().setVisible(true));
    }
}

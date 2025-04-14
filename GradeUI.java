package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import Application_Logic.GradeManager;
import Data_Persistence.GradeRecord;
import Data_Persistence.StudentRecord;

public class GradeUI extends JFrame {
    private DefaultTableModel model;
    private JTable table;
    private JButton cmdAddGradeRecord;
    private JButton cmdUpdateGradeRecord;
    private JButton cmdDeleteGradeRecord;
    private JButton cmdViewGradeDetails;
    private JButton cmdSortByStudent;
    private JButton cmdSortByTeacher;

    public GradeUI() {
        setTitle("Grade Records");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBackground(Color.decode("#A3BFDD"));

        JPanel pnlCommand = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlCommand.setBackground(Color.decode("#A3BFDD"));
        pnlCommand.setForeground(Color.decode("#A3BFDD"));
        cmdAddGradeRecord = new JButton("Add Grade Record");
        cmdUpdateGradeRecord = new JButton("Update Grade Record");
        cmdDeleteGradeRecord = new JButton("Delete Grade Record");
        cmdViewGradeDetails = new JButton("View Grade Details");
        cmdSortByStudent = new JButton("Sort by Student");
        cmdSortByTeacher = new JButton("Sort by Teacher");

        cmdAddGradeRecord.setBackground(Color.decode("#A31621"));
        cmdAddGradeRecord.setForeground(Color.WHITE);
        cmdAddGradeRecord.setBorder(null);

        cmdUpdateGradeRecord.setBackground(Color.decode("#A31621"));
        cmdUpdateGradeRecord.setForeground(Color.WHITE);
        cmdUpdateGradeRecord.setBorder(null);

        cmdDeleteGradeRecord.setBackground(Color.decode("#A31621"));
        cmdDeleteGradeRecord.setForeground(Color.WHITE);
        cmdDeleteGradeRecord.setBorder(null);

        cmdViewGradeDetails.setBackground(Color.decode("#A31621"));
        cmdViewGradeDetails.setForeground(Color.WHITE);
        cmdViewGradeDetails.setBorder(null);

        cmdSortByStudent.setBackground(Color.decode("#A31621"));
        cmdSortByStudent.setForeground(Color.WHITE);
        cmdSortByStudent.setBorder(null);

        cmdSortByTeacher.setBackground(Color.decode("#A31621"));
        cmdSortByTeacher.setForeground(Color.WHITE);
        cmdSortByTeacher.setBorder(null);

        pnlCommand.add(cmdAddGradeRecord);
        pnlCommand.add(cmdUpdateGradeRecord);
        pnlCommand.add(cmdDeleteGradeRecord);
        pnlCommand.add(cmdViewGradeDetails);
        pnlCommand.add(cmdSortByStudent);
        pnlCommand.add(cmdSortByTeacher);
        add(pnlCommand, BorderLayout.SOUTH);

        String[] columnNames = {"Student ID", "First Name", "Last Name", "Teacher", "Monthly Report"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setBackground(Color.decode("#A3BFDD"));
        add(new JScrollPane(table), BorderLayout.CENTER);

        cmdAddGradeRecord.addActionListener(e -> new AddUpdateGradeWindow().setVisible(true));

        cmdUpdateGradeRecord.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                // Extract only First Name (col 1) and Last Name (col 2)
                String firstName = table.getValueAt(selectedRow, 1).toString();
                String lastName  = table.getValueAt(selectedRow, 2).toString();
                // Lookup the GradeRecord using the correct parameters
                GradeRecord record = GradeManager.findGradeRecord(firstName, lastName);
                if (record != null) {
                    new AddUpdateGradeWindow(record, selectedRow).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(GradeUI.this, "Grade record not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(GradeUI.this, "Please select a grade record to update.", "Error", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(GradeUI.this, "Please select a grade record to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cmdViewGradeDetails.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String firstName = table.getValueAt(selectedRow, 1).toString();
                String lastName = table.getValueAt(selectedRow, 2).toString();
                GradeRecord record = GradeManager.findGradeRecord(firstName, lastName);
                if (record != null) {
                    JOptionPane.showMessageDialog(GradeUI.this, record.generateMonthlyReport("Current Month", null), "Grade Details", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(GradeUI.this, "Please select a grade record to view details.", "Error", JOptionPane.ERROR_MESSAGE);
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
        private static final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Constructor for adding a new grade record
        public AddUpdateGradeWindow() {
            setTitle("Add Grade Record");
            setLayout(new BorderLayout(10, 10));
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // Top Panel: Empty fields for new record
            JPanel pnlStudentInfo = new JPanel(new GridLayout(2, 4, 10, 10));
            pnlStudentInfo.add(new JLabel("Student ID:"));
            txtStudentID = new JTextField();
            txtStudentID.setEditable(false); // ID will be auto-generated or linked later
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

            // Action for adding grade entries
            btnAddEntry.addActionListener(e -> {
                String subject = (String) cmbSubject.getSelectedItem();
                String gradeStr = txtGrade.getText().trim();
                String dateStr = txtDate.getText().trim();
                if (gradeStr.isEmpty() || dateStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Grade and Date must be provided.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    int grade = Integer.parseInt(gradeStr);
                    LocalDate date = LocalDate.parse(dateStr, dtFormatter);
                    gradeTableModel.addRow(new Object[]{subject, grade, date.format(dtFormatter)});
                    txtGrade.setText("");
                    txtDate.setText(LocalDate.now().format(dtFormatter));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid grade or date format.", "Error", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(this, "Student and teacher fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (gradeTableModel.getRowCount() == 0) {
                    int confirm = JOptionPane.showConfirmDialog(this, "No grade entries added. Continue?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) return;
                }

                StudentRecord sr = StudentRecord.findStudent(txtStudentFirstName.getText(), txtStudentLastName.getText());
                if (sr == null) {
                    JOptionPane.showMessageDialog(this, "Student not found. Please ensure the student exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                GradeRecord record = new GradeRecord(
                    sr.getId(),
                    txtStudentFirstName.getText(),
                    txtStudentLastName.getText(),
                    txtTeacherAssigned.getText()
                );
                for (int i = 0; i < gradeTableModel.getRowCount(); i++) {
                    String subject = gradeTableModel.getValueAt(i, 0).toString();
                    int grade = Integer.parseInt(gradeTableModel.getValueAt(i, 1).toString());
                    LocalDate date = LocalDate.parse(gradeTableModel.getValueAt(i, 2).toString(), dtFormatter);
                    record.addGrade(subject, grade, date);
                }
                GradeManager.addGradeRecord(record);
                GradeManager.saveAllRecordsToFile();
                refreshTable();
                dispose();
            });

            cmdCancel.addActionListener(e -> dispose());

            pack();
            setLocationRelativeTo(null);
        }

        // Constructor for updating an existing grade record
        public AddUpdateGradeWindow(GradeRecord record, int selectedRow) {
            setTitle("Update Grade Record");
            setLayout(new BorderLayout(10, 10));
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // Top Panel: Student Info pre-populated from GradeRecord
            JPanel pnlStudentInfo = new JPanel(new GridLayout(2, 4, 10, 10));
            pnlStudentInfo.add(new JLabel("Student ID:"));
            txtStudentID = new JTextField(String.valueOf(record.getStudentId()));
            txtStudentID.setEditable(false); // read-only
            pnlStudentInfo.add(txtStudentID);
            pnlStudentInfo.add(new JLabel("First Name:"));
            txtStudentFirstName = new JTextField(record.getStudentFirstName());
            pnlStudentInfo.add(txtStudentFirstName);
            pnlStudentInfo.add(new JLabel("Last Name:"));
            txtStudentLastName = new JTextField(record.getStudentLastName());
            pnlStudentInfo.add(txtStudentLastName);
            pnlStudentInfo.add(new JLabel("Teacher Assigned:"));
            txtTeacherAssigned = new JTextField(record.getTeacherAssigned());
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

            // Action for adding grade entries
            btnAddEntry.addActionListener(e -> {
                String subject = (String) cmbSubject.getSelectedItem();
                String gradeStr = txtGrade.getText().trim();
                String dateStr = txtDate.getText().trim();
                if (gradeStr.isEmpty() || dateStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Grade and Date must be provided.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    int grade = Integer.parseInt(gradeStr);
                    LocalDate date = LocalDate.parse(dateStr, dtFormatter);
                    gradeTableModel.addRow(new Object[]{subject, grade, date.format(dtFormatter)});
                    txtGrade.setText("");
                    txtDate.setText(LocalDate.now().format(dtFormatter));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid grade or date format.", "Error", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(this, "Student and teacher fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (gradeTableModel.getRowCount() == 0) {
                    int confirm = JOptionPane.showConfirmDialog(this, "No grade entries added. Continue?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) return;
                }
                // Update the GradeRecord with new grade entries
                for (int i = 0; i < gradeTableModel.getRowCount(); i++) {
                    String subj = gradeTableModel.getValueAt(i, 0).toString();
                    int grd = Integer.parseInt(gradeTableModel.getValueAt(i, 1).toString());
                    LocalDate dt = LocalDate.parse(gradeTableModel.getValueAt(i, 2).toString(), dtFormatter);
                    record.addGrade(subj, grd, dt);
                }
                // Update teacher assignment if modified
                record.reassignTeacher(txtTeacherAssigned.getText(), LocalDate.now().getYear());
                GradeManager.saveAllRecordsToFile();
                StudentRecord sr = StudentRecord.findStudent(txtStudentFirstName.getText(), txtStudentLastName.getText());
                if (sr != null) sr.updateIndividualFile();
                refreshTable();
                dispose();
            });

            cmdCancel.addActionListener(e -> dispose());

            pack();
            setLocationRelativeTo(null);
        }
    }
}

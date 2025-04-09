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

import Data_Persistence.GradeRecord;
import Application_Logic.GradeManager;
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

        String[] columnNames = {"Student ID", "First Name", "Last Name", "Teacher", "Monthly Report"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        cmdAddGradeRecord.addActionListener(e -> new AddUpdateGradeWindow().setVisible(true));

        cmdUpdateGradeRecord.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String[] gradeData = new String[4];
                for (int i = 0; i < 4; i++) {
                    gradeData[i] = table.getValueAt(selectedRow, i).toString();
                }
                new AddUpdateGradeWindow(gradeData, selectedRow).setVisible(true);
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
                gr.generateMonthlyReport("Current Month", null)
            });
        }
    }

    private class AddUpdateGradeWindow extends JFrame {
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

        public AddUpdateGradeWindow() {
            this(null, -1);
        }

        public AddUpdateGradeWindow(String[] gradeData, int selectedRow) {
            setTitle(gradeData == null ? "Add Grade Record" : "Update Grade Record");
            setLayout(new BorderLayout(10, 10));
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // Add padding around the window
            JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setContentPane(contentPanel);

            // Top Panel: Student Info
            JPanel pnlStudentInfo = new JPanel(new GridLayout(2, 3, 10, 10));
            pnlStudentInfo.add(new JLabel("First Name:"));
            pnlStudentInfo.add(new JLabel("Last Name:"));
            pnlStudentInfo.add(new JLabel("Teacher Assigned:"));
            txtStudentFirstName = new JTextField();
            pnlStudentInfo.add(txtStudentFirstName);
            txtStudentLastName = new JTextField();
            pnlStudentInfo.add(txtStudentLastName);
            txtTeacherAssigned = new JTextField();
            pnlStudentInfo.add(txtTeacherAssigned);
            contentPanel.add(pnlStudentInfo, BorderLayout.NORTH);

            // Center Panel: Grade Entry Section
            JPanel pnlEntry = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            cmbSubject = new JComboBox<>(GradeRecord.SUBJECTS.toArray(new String[0]));
            pnlEntry.add(new JLabel("Subject:"));
            pnlEntry.add(cmbSubject);
            txtGrade = new JTextField(5);
            pnlEntry.add(new JLabel("Grade:"));
            pnlEntry.add(txtGrade);
            txtDate = new JTextField(10);
            txtDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            pnlEntry.add(new JLabel("Date (yyyy-MM-dd):"));
            pnlEntry.add(txtDate);
            btnAddEntry = new JButton("Add Entry");
            pnlEntry.add(btnAddEntry);
            btnRemoveEntry = new JButton("Remove Selected");
            pnlEntry.add(btnRemoveEntry);
            contentPanel.add(pnlEntry, BorderLayout.CENTER);

            // Table for grade entries
            String[] columns = {"Subject", "Grade", "Date"};
            gradeTableModel = new DefaultTableModel(columns, 0);
            gradeTable = new JTable(gradeTableModel);
            contentPanel.add(new JScrollPane(gradeTable), BorderLayout.SOUTH);

            // Bottom Panel: Save/Cancel
            JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            cmdSave = new JButton("Save");
            cmdCancel = new JButton("Cancel");
            pnlButtons.add(cmdSave);
            pnlButtons.add(cmdCancel);
            contentPanel.add(pnlButtons, BorderLayout.PAGE_END);

            // Button Actions
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
                    LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    gradeTableModel.addRow(new Object[]{subject, grade, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))});
                    txtGrade.setText("");
                    txtDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
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

                // Save logic
                GradeRecord record = GradeManager.findGradeRecord(txtStudentFirstName.getText(), txtStudentLastName.getText());
                if (record == null) {
                    record = new GradeRecord(
                        txtStudentFirstName.getText(),
                        txtStudentLastName.getText(),
                        txtTeacherAssigned.getText()
                    );
                    GradeManager.addGradeRecord(record);
                }
                for (int i = 0; i < gradeTableModel.getRowCount(); i++) {
                    String subject = gradeTableModel.getValueAt(i, 0).toString();
                    int grade = Integer.parseInt(gradeTableModel.getValueAt(i, 1).toString());
                    LocalDate date = LocalDate.parse(gradeTableModel.getValueAt(i, 2).toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    record.addGrade(subject, grade, date);
                }
                GradeManager.saveAllRecordsToFile();
                refreshTable();
                dispose();
            });

            cmdCancel.addActionListener(e -> dispose());

            // Pre-fill fields if updating
            if (gradeData != null) {
                txtStudentFirstName.setText(gradeData[0]);
                txtStudentLastName.setText(gradeData[1]);
                txtTeacherAssigned.setText(gradeData[2]);
            }

            setPreferredSize(new Dimension(800, 600));
            pack();
            setLocationRelativeTo(null);
        }
    }
}

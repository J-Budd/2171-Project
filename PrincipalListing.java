package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import Data_Persistence.StudentRecord;
import Application_Logic.StudentManager;

public class PrincipalListing extends JFrame {
    private JButton cmdViewStudentRecords;
    private JButton cmdViewStudentAttendance;
    private JButton cmdViewStudentGrades;
    private JButton cmdViewExpenses;

    public PrincipalListing() {
        setTitle("Principal Dashboard");
        setLayout(new GridLayout(4, 1, 10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        cmdViewStudentRecords = new JButton("View Student Records");
        cmdViewStudentAttendance = new JButton("View Student Attendance");
        cmdViewStudentGrades = new JButton("View Student Grades");
        cmdViewExpenses = new JButton("View Expenses");

        cmdViewStudentRecords.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new StudentRecordsWindow().setVisible(true);
            }
        });

        add(cmdViewStudentRecords);
        add(cmdViewStudentAttendance);
        add(cmdViewStudentGrades);
        add(cmdViewExpenses);

        pack();
        setLocationRelativeTo(null);
    }

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
                        String[] studentData = new String[7]; // Adjusted array size to 7
                        for (int i = 0; i < 7; i++) { // Adjusted loop to 7 iterations
                            studentData[i] = (String) table.getValueAt(selectedRow, i);
                        }
                        new AddUpdateStudentWindow(studentData, selectedRow).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(StudentRecordsWindow.this, "Please select a student to update.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            /*

            cmdDeleteStudent.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        String firstName = (String) table.getValueAt(selectedRow, 0);
                        String lastName = (String) table.getValueAt(selectedRow, 1);
                        StudentManager.deleteStudent(firstName, lastName);
                        model.removeRow(selectedRow);
                    } else {
                        JOptionPane.showMessageDialog(StudentRecordsWindow.this, "Please select a student to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            cmdSortByLastName.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    StudentManager.getAllStudents().sort(Comparator.comparing(StudentRecord::getLastName));
                    refreshTable();
                }
            });

            cmdSortByBirthyear.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    StudentManager.getAllStudents().sort(Comparator.comparing(StudentRecord::getBirthdate));
                    refreshTable();
                }
            });
*/

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
                setLayout(new GridLayout(8, 2, 10, 10)); // Adjusted grid layout to 8 rows
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                String[] labels = {"First Name:", "Last Name:", "Birth Date:", "Address:", "Guardian:", "Regular Contact:", "Emergency Contact:"};
                textFields = new JTextField[7]; // Adjusted textFields array size

                for (int i = 0; i < 7; i++) { // Adjusted loop to 7 iterations
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
                                String oldFirstName = (String) table.getValueAt(selectedRow, 0);
                                String oldLastName = (String) table.getValueAt(selectedRow, 1);
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

                cmdCancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });

                add(cmdSave);
                add(cmdCancel);

                pack();
                setLocationRelativeTo(null);
            }
        }
    }
}

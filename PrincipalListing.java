package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
                    new AddUpdateStudentWindow(null).setVisible(true);
                }
            });

            cmdUpdateStudent.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        String[] studentData = new String[8];
                        for (int i = 0; i < 8; i++) {
                            studentData[i] = (String) table.getValueAt(selectedRow, i);
                        }
                        new AddUpdateStudentWindow(studentData).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(StudentRecordsWindow.this, "Please select a student to update.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            pnlCommand.add(cmdAddStudent);
            pnlCommand.add(cmdUpdateStudent);
            pnlCommand.add(cmdDeleteStudent);
            pnlCommand.add(cmdSortByLastName);
            pnlCommand.add(cmdSortByBirthyear);

            String[] columnNames = {"First Name", "Last Name", "Birth Date", "Address", "Guardian", "Regular Contact", "Emergency Contact", "Class"};
            model = new DefaultTableModel(columnNames, 0);
            table = new JTable(model);

            pnlDisplay.add(new JScrollPane(table), BorderLayout.CENTER);

            add(pnlDisplay, BorderLayout.CENTER);
            add(pnlCommand, BorderLayout.SOUTH);

            pack();
            setLocationRelativeTo(null);
        }

        private class AddUpdateStudentWindow extends JFrame {
            private JTextField[] textFields;
            private JButton cmdSave;
            private JButton cmdCancel;

            public AddUpdateStudentWindow(String[] studentData) {
                setTitle(studentData == null ? "Add Student Record" : "Update Student Record");
                setLayout(new GridLayout(9, 2, 10, 10));
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                String[] labels = {"First Name:", "Last Name:", "Birth Date:", "Address:", "Guardian:", "Regular Contact:", "Emergency Contact:", "Class:"};
                textFields = new JTextField[8];

                for (int i = 0; i < 8; i++) {
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
                            // Commented out the immediate addition of rows
                            // model.addRow(new Object[]{
                            //     textFields[0].getText(),
                            //     textFields[1].getText(),
                            //     textFields[2].getText(),
                            //     textFields[3].getText(),
                            //     textFields[4].getText(),
                            //     textFields[5].getText(),
                            //     textFields[6].getText(),
                            //     textFields[7].getText()
                            // });
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

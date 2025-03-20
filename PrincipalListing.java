package UI;

import javax.swing.*;
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

            pnlCommand.add(cmdAddStudent);
            pnlCommand.add(cmdUpdateStudent);
            pnlCommand.add(cmdDeleteStudent);
            pnlCommand.add(cmdSortByLastName);
            pnlCommand.add(cmdSortByBirthyear);

            // Placeholder for student records display
            pnlDisplay.add(new JLabel("Student Records Display"), BorderLayout.CENTER);

            add(pnlDisplay, BorderLayout.CENTER);
            add(pnlCommand, BorderLayout.SOUTH);

            pack();
            setLocationRelativeTo(null);
        }
    }
}

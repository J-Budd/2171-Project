package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeacherListing extends JFrame {
    private JButton btnViewAttendance;
    private JButton btnViewGrades;
    private JButton btnClose;

    public TeacherListing() {
        setTitle("Teacher Dashboard");
        setLayout(new BorderLayout());
        // Adjusted layout size for better readability and usability
        setPreferredSize(new Dimension(1000, 100));

        // Greeting Panel
        JPanel pnlGreeting = new JPanel();
        JLabel lblGreeting = new JLabel("Welcome to the Teacher Dashboard");
        pnlGreeting.add(lblGreeting);
        add(pnlGreeting, BorderLayout.NORTH);

        // Command Panel
        JPanel pnlCommand = new JPanel();
        btnViewAttendance = new JButton("View Attendance");
        btnViewGrades = new JButton("View Grades");
        btnClose = new JButton("Close");

        pnlCommand.add(btnViewAttendance);
        pnlCommand.add(btnViewGrades);
        pnlCommand.add(btnClose);
        add(pnlCommand, BorderLayout.SOUTH);

        // Button Actions
        btnViewAttendance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AttendanceScreen().setVisible(true);
            }
        });

        btnViewGrades.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GradeUI().setVisible(true);
            }
        });

        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TeacherListing().setVisible(true));
    }
}

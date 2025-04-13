package UI;

import Application_Logic.AttendanceManager;
import Application_Logic.GradeManager;
import Data_Persistence.AttendanceRecord;
import Data_Persistence.GradeRecord;

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
        setBackground(Color.decode("#A3BFDD"));
        setPreferredSize(new Dimension(1000, 100));

        // Greeting Panel
        JPanel pnlGreeting = new JPanel();
        pnlGreeting.setBackground(Color.decode("#A3BFDD"));
        pnlGreeting.setForeground(Color.decode("#A3BFDD"));
        JLabel lblGreeting = new JLabel("Welcome to the Teacher Dashboard");
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
        btnClose = new JButton("Close");

        btnViewAttendance.setBackground(Color.decode("#A31621"));
        btnViewAttendance.setForeground(Color.WHITE);
        btnViewAttendance.setBorder(null);
        btnViewAttendance.setPreferredSize(new Dimension(100, 20));
        btnViewGrades.setBackground(Color.decode("#A31621"));
        btnViewGrades.setForeground(Color.WHITE);
        btnViewGrades.setBorder(null);
        btnViewGrades.setPreferredSize(new Dimension(100, 20));
        btnClose.setBackground(Color.decode("#A31621"));
        btnClose.setForeground(Color.WHITE);
        btnClose.setBorder(null);
        btnClose.setPreferredSize(new Dimension(100, 20));

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

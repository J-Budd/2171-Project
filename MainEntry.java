package UI;

// Used GitHub Copilot in VScode to separate the old 2140 code to follow the Package Diagram
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Data_Persistence.UserRecord;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainEntry extends JFrame {
    private LoginScreen loginScreen; // Reference to the LoginScreen instance

    private JLabel screenDescription; // Label displaying the screen description
    
    private JButton cmdSignIn; // Button for signing in
    private JButton cmdSignUp; // Button for signing up
    private JButton cmdClose; // Button for closing the application

    private JPanel panel; // Panel containing UI elements

    private MainEntry thisForm; // Reference to the current MainEntry instance

    public MainEntry() {
        thisForm = this;

        panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(new EmptyBorder(50, 50, 50, 50));

        screenDescription = new JLabel("Lakespen Basic School Database Manager", SwingConstants.CENTER);
        cmdSignIn = new JButton("Sign In");
        cmdSignUp = new JButton("Sign Up");
        cmdClose = new JButton("Close");

        screenDescription.setFont(new Font("Arial", Font.BOLD, 18));

        cmdClose.setBackground(Color.DARK_GRAY);
        cmdClose.setForeground(Color.WHITE);
        cmdClose.setPreferredSize(new Dimension(150, 50));

        cmdSignIn.setBackground(Color.DARK_GRAY);
        cmdSignIn.setForeground(Color.WHITE);
        cmdSignIn.setPreferredSize(new Dimension(150, 50));

        cmdSignUp.setBackground(Color.DARK_GRAY);
        cmdSignUp.setForeground(Color.WHITE);
        cmdSignUp.setPreferredSize(new Dimension(150, 50));

        cmdClose.addActionListener(new CloseButtonListener());
        cmdSignIn.addActionListener(new cmdSignInButtonListener());
        cmdSignUp.addActionListener(new cmdSignUpButtonListener());

        panel.add(screenDescription);
        panel.add(cmdSignIn);
        panel.add(cmdSignUp);
        panel.add(cmdClose);
        panel.setPreferredSize(new Dimension(500, 400));

        add(panel);
        pack();
        setContentPane(panel);
    }

    private static void createAndShowGUI() {
        MainEntry mainEntry = new MainEntry();
        mainEntry.setPreferredSize(new Dimension(500, 400));
        mainEntry.setResizable(false);
        mainEntry.setLocationRelativeTo(null);
        mainEntry.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                UserRecord.loadExistingUsers();
                createAndShowGUI();
            }
        });
    }

    private class cmdSignInButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            loginScreen = new LoginScreen(true, thisForm);
            loginScreen.setVisible(true);
            setVisible(false);
        }
    }

    private class cmdSignUpButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            loginScreen = new LoginScreen(false, thisForm);
            loginScreen.setVisible(true);
            setVisible(false);
        }
    }

    private class CloseButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}

package UI;

import Application_Logic.LoginManager;
import Data_Persistence.UserRecord;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainEntry extends JFrame {
    private LoginScreen loginScreen; // Reference to the LoginScreen instance

    private JLabel screenDescription; // Label displaying the screen description
    private JLabel imageLabel; // Label to display the image
    
    private JButton cmdSignIn; // Button for signing in
    private JButton cmdSignUp; // Button for signing up
    private JButton cmdClose; // Button for closing the application

    private JPanel panel; // Panel containing UI elements

    private MainEntry thisForm; // Reference to the current MainEntry instance

    public MainEntry() {
        thisForm = this;

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(50, 50, 50, 50));
        panel.setBackground(Color.decode("#A3BFDD"));
        panel.setForeground(Color.decode("#A3BFDD"));

        screenDescription = new JLabel("Lakespen Basic School Database Manager", SwingConstants.CENTER);
        screenDescription.setForeground(Color.decode("#191919")); 
        screenDescription.setFont(new Font("Arial", Font.BOLD, 24));

        imageLabel = new JLabel(new ImageIcon(new ImageIcon("Resources/SchoolCrest.jpg")
            .getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH)));
        cmdSignIn = new JButton("Sign In");
        cmdSignUp = new JButton("Sign Up");
        cmdClose = new JButton("Close");

        cmdClose.setBackground(Color.decode("#A31621"));
        cmdClose.setForeground(Color.WHITE);
        cmdClose.setMaximumSize(new Dimension(900, 100));
        cmdClose.setBorder(null);

        cmdSignIn.setBackground(Color.decode("#A31621"));
        cmdSignIn.setForeground(Color.WHITE);
        cmdSignIn.setMaximumSize(new Dimension(900, 100));
        cmdSignIn.setBorder(null);

        cmdSignUp.setBackground(Color.decode("#A31621"));
        cmdSignUp.setForeground(Color.WHITE);
        cmdSignUp.setMaximumSize(new Dimension(900, 100));
        cmdSignUp.setBorder(null);

        cmdClose.addActionListener(new CloseButtonListener());
        cmdSignIn.addActionListener(new cmdSignInButtonListener());
        cmdSignUp.addActionListener(new cmdSignUpButtonListener());

        screenDescription.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cmdSignIn.setAlignmentX(Component.CENTER_ALIGNMENT);
        cmdSignUp.setAlignmentX(Component.CENTER_ALIGNMENT);
        cmdClose.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(screenDescription);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(imageLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(cmdSignIn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(cmdSignUp);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(cmdClose);
        panel.setPreferredSize(new Dimension(1000, 700));

        add(panel);
        pack();
        setContentPane(panel);
        setTitle("Main Entry");
        setLocationRelativeTo(null);
    }

    private static void createAndShowGUI() {
        MainEntry mainEntry = new MainEntry();
        mainEntry.setPreferredSize(new Dimension(1000, 800));
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

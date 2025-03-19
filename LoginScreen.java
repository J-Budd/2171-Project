package UI;

// Used GitHub Copilot in VScode to separate the old 2140 code to follow the Package Diagram
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import Application_Logic.LoginManager;

public class LoginScreen extends JFrame {
    private boolean signIn;
    private MainEntry mainScreen;
    private JLabel screenDescription;
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField secondPasswordField;
    private JTextField roleField;
    private JButton cmdBack;
    private JButton cmdEnter;
    private JPanel mainPanel;
    private JPanel secondPanel;

    public LoginScreen(boolean signIn, MainEntry mainScreen) {
        this.signIn = signIn;
        this.mainScreen = mainScreen;

        setLayout(new BorderLayout());

        mainPanel = new JPanel(new GridLayout(0, 1));
        secondPanel = new JPanel(new GridLayout(7, 2, 40, 10));

        screenDescription = new JLabel(signIn ? "Sign-In" : "Sign-Up");
        JLabel spacer = new JLabel("");
        usernameField = new JTextField();
        passwordField = new JTextField();
        cmdBack = new JButton("Back");
        cmdEnter = new JButton("Enter");

        screenDescription.setFont(new Font("Arial", Font.BOLD, 24));

        cmdBack.setBackground(Color.DARK_GRAY);
        cmdBack.setForeground(Color.WHITE);
        cmdEnter.setBackground(Color.DARK_GRAY);
        cmdEnter.setForeground(Color.WHITE);

        cmdEnter.addActionListener(new cmdEnterButtonListener());
        cmdBack.addActionListener(new cmdBackButtonListener());

        secondPanel.add(screenDescription);
        secondPanel.add(spacer);
        secondPanel.add(new JLabel("Username:"));
        secondPanel.add(usernameField);
        secondPanel.add(new JLabel("Password:"));
        secondPanel.add(passwordField);

        if (!signIn) {
            addSecondaryComponents();
        }
        secondPanel.add(cmdEnter);
        secondPanel.add(cmdBack);

        mainPanel.setPreferredSize(new Dimension(1000, 600));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(secondPanel);
        add(mainPanel);
        pack();

        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void addSecondaryComponents() {
        secondPasswordField = new JTextField();
        secondPanel.add(new JLabel("Re-enter Password: "));
        secondPanel.add(secondPasswordField);
        
        roleField = new JTextField();
        secondPanel.add(new JLabel("User's Role (Principal/Teacher/Cook): "));
        secondPanel.add(roleField);
    }

    public void showPopUp(String errorMessage) {
        JOptionPane.showMessageDialog(rootPane, errorMessage);
    }

    private class cmdBackButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            mainScreen.setVisible(true);
            dispose();
        }
    }

    private class cmdEnterButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            LoginManager.handleLogin(signIn, usernameField.getText(), passwordField.getText(), secondPasswordField != null ? secondPasswordField.getText() : null, roleField != null ? roleField.getText() : null, mainScreen, LoginScreen.this);
        }
    }
}

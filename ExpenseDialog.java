package expense;

import javax.swing.*;
import java.awt.*;

public class ExpenseDialog extends JDialog {
    private JTextField amountField;
    private JTextField descriptionField;
    private JTextField typeField;

    private ExpenseManager manager;
    private int index;

    public ExpenseDialog(JFrame parent, Expense expense, int index) {
        super(parent, true);
        this.manager = ((ExpenseGUI) parent).manager;
        this.index = index;

        setTitle(index >= 0 ? "Edit Expense" : "Add Expense");
        setLayout(new GridLayout(4, 2, 10, 10));
        setSize(300, 200);
        setLocationRelativeTo(parent);

        amountField = new JTextField();
        descriptionField = new JTextField();
        typeField = new JTextField();

        if (expense != null) {
            amountField.setText(String.valueOf(expense.getAmount()));
            descriptionField.setText(expense.getDescription());
            typeField.setText(expense.getType());
        }

        add(new JLabel("Amount:"));
        add(amountField);
        add(new JLabel("Description:"));
        add(descriptionField);
        add(new JLabel("Type:"));
        add(typeField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveExpense());
        add(saveButton);
    }

    private void saveExpense() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String desc = descriptionField.getText();
            String type = typeField.getText();

            Expense expense = new Expense(amount, desc, type);
            if (index >= 0) {
                manager.editExpense(index, expense);
            } else {
                manager.addExpense(expense);
            }

            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount.");
        }
    }
}

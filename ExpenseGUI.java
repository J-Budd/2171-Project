package expense;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExpenseGUI extends JFrame {

    ExpenseManager manager;
    private JTable expenseTable;
    private ExpenseTableModel tableModel;

    public ExpenseGUI() {
        manager = new ExpenseManager();

        setTitle("Expense Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);

        tableModel = new ExpenseTableModel(manager.getExpenses());
        expenseTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(expenseTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton reportButton = new JButton("Termly Report"); // 📌 Added

        // Add button
        addButton.addActionListener(e -> {
            ExpenseDialog dialog = new ExpenseDialog(this, null, -1);
            dialog.setVisible(true);
            tableModel.updateData(manager.getExpenses());
        });

        // Edit button
        editButton.addActionListener(e -> {
            int selectedRow = expenseTable.getSelectedRow();
            if (selectedRow >= 0) {
                Expense expense = manager.getExpenses().get(selectedRow);
                ExpenseDialog dialog = new ExpenseDialog(this, expense, selectedRow);
                dialog.setVisible(true);
                tableModel.updateData(manager.getExpenses());
            } else {
                JOptionPane.showMessageDialog(this, "Please select an expense to edit.");
            }
        });

        // Delete button
        deleteButton.addActionListener(e -> {
            int selectedRow = expenseTable.getSelectedRow();
            if (selectedRow >= 0) {
                int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this expense?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    manager.deleteExpense(selectedRow);
                    tableModel.updateData(manager.getExpenses());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an expense to delete.");
            }
        });

        // 📌 Termly Report button
        reportButton.addActionListener(e -> {
            String report = manager.generateTermlyReport();
            JTextArea textArea = new JTextArea(report);
            textArea.setEditable(false);
            JScrollPane reportScroll = new JScrollPane(textArea);
            reportScroll.setPreferredSize(new Dimension(600, 300));
            JOptionPane.showMessageDialog(this, reportScroll, "Termly Report", JOptionPane.INFORMATION_MESSAGE);
        });

        // Add all buttons to panel
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(reportButton); // 📌 Added to panel

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExpenseGUI().setVisible(true));
    }
}

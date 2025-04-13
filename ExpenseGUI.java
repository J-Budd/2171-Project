package UI;

import Application_Logic.Expense;
import Application_Logic.ExpenseManager;
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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);

        getContentPane().setBackground(Color.decode("#A3BFDD"));

        tableModel = new ExpenseTableModel(manager.getExpenses());
        expenseTable = new JTable(tableModel);
        expenseTable.setBackground(Color.decode("#A3BFDD"));
        expenseTable.setForeground(Color.decode("#A3BFDD"));

        JScrollPane scrollPane = new JScrollPane(expenseTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.decode("#A3BFDD"));
        buttonPanel.setForeground(Color.decode("#A3BFDD"));

        JButton addButton = new JButton("Add");
        addButton.setBackground(Color.decode("#A31621"));
        addButton.setForeground(Color.WHITE);
        addButton.setBorder(null);

        JButton editButton = new JButton("Edit");
        editButton.setBackground(Color.decode("#A31621"));
        editButton.setForeground(Color.WHITE);
        editButton.setBorder(null);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(Color.decode("#A31621"));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBorder(null);

        JButton reportButton = new JButton("Termly Report");
        reportButton.setBackground(Color.decode("#A31621"));
        reportButton.setForeground(Color.WHITE);
        reportButton.setBorder(null);

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

        // Termly Report button
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
        buttonPanel.add(reportButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExpenseGUI().setVisible(true));
    }
}

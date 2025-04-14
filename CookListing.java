package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import Application_Logic.InventoryManager;
import Data_Persistence.InventoryRecord;

public class CookListing extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JButton cmdAddItem;
    private JButton cmdUpdateItem;
    private InventoryManager manager;

    public CookListing() {
        setTitle("Cook's Inventory Dashboard");
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 400));
        setBackground(Color.decode("#A3BFDD"));

        manager = new InventoryManager();

        // Command Panel
        JPanel pnlCommand = new JPanel(new FlowLayout());
        pnlCommand.setBackground(Color.decode("#A3BFDD"));
        pnlCommand.setForeground(Color.decode("#A3BFDD"));
        cmdAddItem = new JButton("Add Inventory Item");
        cmdUpdateItem = new JButton("Update Inventory Item");

        cmdAddItem.setBackground(Color.decode("#A31621"));
        cmdAddItem.setForeground(Color.WHITE);
        cmdAddItem.setBorder(null);
        cmdUpdateItem.setBackground(Color.decode("#A31621"));
        cmdUpdateItem.setForeground(Color.WHITE);
        cmdUpdateItem.setBorder(null);

        pnlCommand.add(cmdAddItem);
        pnlCommand.add(cmdUpdateItem);

        // Table setup
        String[] columnNames = {"ID", "Item", "Category", "Quantity"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setBackground(Color.decode("#A3BFDD"));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        add(pnlCommand, BorderLayout.SOUTH);

        refreshTable();

        // Button Actions
        cmdAddItem.addActionListener(e -> new AddUpdateItemWindow(null, -1).setVisible(true));
        cmdUpdateItem.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String[] itemData = new String[4];
                for (int i = 0; i < 4; i++) {
                    itemData[i] = table.getValueAt(selectedRow, i).toString();
                }
                new AddUpdateItemWindow(itemData, selectedRow).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an item to update.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void refreshTable() {
        model.setRowCount(0);
        List<InventoryRecord> items = manager.getInventory();
        for (InventoryRecord item : items) {
            if (item.getQuantity() > 0) {
                model.addRow(new Object[]{
                    item.getId(),
                    item.getName(),
                    item.getCategory(),
                    item.getQuantity()
                });
            }
        }
    }

    private class AddUpdateItemWindow extends JFrame {
        private JTextField txtName, txtCategory, txtQuantity;
        private JComboBox<String> cmbAction;
        private JButton cmdSave, cmdCancel;
        private int selectedRow;
        private boolean isWindowOpen = false;

        public AddUpdateItemWindow(String[] itemData, int selectedRow) {
            if (isWindowOpen) return;
            isWindowOpen = true;

            this.selectedRow = selectedRow;
            setTitle(itemData == null ? "Add Inventory Item" : "Update Inventory Item");
            setLayout(new GridLayout(5, 2, 10, 10));
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            add(new JLabel("Item Name:"));
            txtName = new JTextField(itemData != null ? itemData[1] : "");
            add(txtName);

            add(new JLabel("Category:"));
            txtCategory = new JTextField(itemData != null ? itemData[2] : "");
            add(txtCategory);

            add(new JLabel("Action:"));
            cmbAction = new JComboBox<>(new String[]{"Add", "Remove"});
            add(cmbAction);

            add(new JLabel("Quantity:"));
            txtQuantity = new JTextField(itemData != null ? itemData[3] : "");
            add(txtQuantity);

            cmdSave = new JButton("Save");
            cmdCancel = new JButton("Cancel");

            cmdSave.addActionListener(e -> {
                String name = txtName.getText().trim();
                String category = txtCategory.getText().trim();
                String quantityText = txtQuantity.getText().trim();
                boolean isAdding = cmbAction.getSelectedItem().equals("Add");

                if (name.isEmpty() || category.isEmpty() || quantityText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int quantity = Integer.parseInt(quantityText);
                    String result = itemData == null
                        ? manager.addItem(name, category, quantity)
                        : manager.updateItem(name, quantity, isAdding);

                    if (result.startsWith("Error")) {
                        JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        refreshTable();
                        dispose();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Quantity must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            cmdCancel.addActionListener(e -> dispose());

            add(cmdSave);
            add(cmdCancel);

            pack();
            setLocationRelativeTo(null);

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    isWindowOpen = false;
                }
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CookListing().setVisible(true));
    }
}

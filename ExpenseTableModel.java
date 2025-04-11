package expense;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ExpenseTableModel extends AbstractTableModel {
    private final String[] columns = {"Amount", "Description", "Type", "Date"};
    private ArrayList<Expense> expenses;

    public ExpenseTableModel(ArrayList<Expense> expenses) {
        this.expenses = expenses;
    }

    @Override
    public int getRowCount() {
        return expenses.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Expense e = expenses.get(row);
        return switch (col) {
            case 0 -> e.getAmount();
            case 1 -> e.getDescription();
            case 2 -> e.getType();
            case 3 -> new SimpleDateFormat("yyyy-MM-dd").format(e.getDate());
            default -> null;
        };
    }

    @Override
    public String getColumnName(int index) {
        return columns[index];
    }

    public void updateData(ArrayList<Expense> updatedList) {
        this.expenses = updatedList;
        fireTableDataChanged();
    }
}

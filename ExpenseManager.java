package expense;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ExpenseManager {

    private static ArrayList<Expense> expenses;

    public ExpenseManager() {
        expenses = ExpenseRecord.loadExpenses();
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
        saveExpenses();
    }

    public void editExpense(int index, Expense updatedExpense) {
        if (index >= 0 && index < expenses.size()) {
            expenses.set(index, updatedExpense);
            saveExpenses();
        }
    }

    public void deleteExpense(int index) {
        if (index >= 0 && index < expenses.size()) {
            expenses.remove(index);
            saveExpenses();
        }
    }

    public void saveExpenses() {
        ExpenseRecord.saveExpenses(expenses);
    }

    // ✅ Generate report for last 3 months
    public static String generateTermlyReport() {
        StringBuilder report = new StringBuilder();
        double total = 0;

        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        report.append("Termly Report (Last 3 Months)\n");
        report.append(String.format("%-10s %-20s %-20s %-15s\n", "Amount", "Description", "Type", "Date"));
        report.append("------------------------------------------------------------\n");

        for (Expense exp : expenses) {
            calendar.setTime(exp.getDate());
            int expMonth = calendar.get(Calendar.MONTH);
            int expYear = calendar.get(Calendar.YEAR);

            boolean isInLastThreeMonths = (currentYear == expYear && expMonth >= currentMonth - 2)
                                        || (currentYear == expYear && currentMonth < 2 && expMonth >= 12 - (2 - currentMonth))
                                        || (expYear == currentYear - 1 && currentMonth < 2 && expMonth >= 12 - (2 - currentMonth));

            if (isInLastThreeMonths) {
                total += exp.getAmount();
                report.append(String.format("%-10.2f %-20s %-20s %-15s\n",
                        exp.getAmount(),
                        exp.getDescription(),
                        exp.getType(),
                        new SimpleDateFormat("yyyy-MM-dd").format(exp.getDate())));
            }
        }

        report.append("------------------------------------------------------------\n");
        report.append(String.format("Total Amount: %.2f\n", total));

        return report.toString();
    }
}

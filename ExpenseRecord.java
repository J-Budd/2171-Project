package Data_Persistence;

import Application_Logic.Expense;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseRecord {
    private static final String FILE_PATH = "Data/Expense/expenses.dat";

    @SuppressWarnings("unchecked") // Suppress unchecked cast warning
    public static ArrayList<Expense> loadExpenses() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Object obj = ois.readObject();
            if (obj instanceof ArrayList<?>) {
                return (ArrayList<Expense>) obj; // Safe cast
            }
        } catch (IOException | ClassNotFoundException e) {
            // Handle exception
        }
        return new ArrayList<>();
    }

    public static void saveExpenses(List<Expense> expenses) {
        try {
            // Create the directory if it doesn't exist
            File file = new File(FILE_PATH);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();  // This creates the full path
            }

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(expenses);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package Application_Logic;

import Data_Persistence.InventoryRecord;
import java.io.*;
import java.util.*;

public class InventoryManager {
    private List<InventoryRecord> inventory;
    private String logFilePath = "Data/Inventory/inventory_log.txt";
    private String inventoryFilePath = "Data/Inventory/inventory.txt";
    private String usageReportFilePath = "Data/Inventory/usage_report.txt";

    // Constructor initializes inventory list and loads data from file
    public InventoryManager() {
        inventory = new ArrayList<>();
        loadInventoryFromFile();
    }

    // Ensures the directory exists before saving or loading files
    private void ensureDirectoryExists(String filePath) {
        File directory = new File(filePath).getParentFile();
        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }
    }

    // Returns the full list of inventory items
    public List<InventoryRecord> getInventory() {
        return inventory;
    }

    // Finds an item by name (case-insensitive)
    public InventoryRecord findItemByName(String name) {
        for (InventoryRecord item : inventory) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    // Adds a new item, or updates quantity if item already exists
    public String addItem(String name, String category, int quantity) {
        if (name == null || name.isEmpty() || category == null || category.isEmpty()) {
            return "Error: Item name and category must be provided.";
        }

        for (InventoryRecord item : inventory) {
            if (item.getName().equalsIgnoreCase(name)) {
                return updateItem(name, quantity, true);
            }
        }

        InventoryRecord newItem = new InventoryRecord(name, category, quantity);
        inventory.add(newItem);
        logTransaction("Added item: " + name + ", Category: " + category + ", Quantity: " + quantity);
        sortInventoryByQuantity();
        saveInventoryToFile();
        return "Item added successfully.";
    }

    // Adds a new item with an explicit ID
    public void addItemWithId(int id, String name, String category, int quantity) {
        InventoryRecord newItem = new InventoryRecord(id, name, category, quantity);
        inventory.add(newItem);
        logTransaction("Added item with ID: " + id + ", Name: " + name + ", Category: " + category + ", Quantity: " + quantity);
        sortInventoryByQuantity();
        saveInventoryToFile();
    }

    // Updates quantity of an existing item (either adds or subtracts)
    public String updateItem(String name, int quantityChange, boolean isAdding) {
        if (name == null || name.isEmpty()) {
            return "Error: Item name must be provided.";
        }

        for (Iterator<InventoryRecord> iterator = inventory.iterator(); iterator.hasNext(); ) {
            InventoryRecord item = iterator.next();
            if (item.getName().equalsIgnoreCase(name)) {
                int newQuantity = isAdding ? item.getQuantity() + quantityChange : item.getQuantity() - quantityChange;
                if (newQuantity < 0) {
                    return "Error: Insufficient quantity to remove.";
                }
                if (newQuantity == 0) {
                    iterator.remove();
                    logTransaction("Removed item: " + name + " as its quantity reached 0.");
                } else {
                    item.setQuantity(newQuantity);
                    logTransaction((isAdding ? "Added " : "Removed ") + quantityChange + " of item: " + name + ", New Quantity: " + newQuantity);
                }
                sortInventoryByQuantity();
                saveInventoryToFile();
                return "Item updated successfully.";
            }
        }
        return "Error: Item not found.";
    }

    // Deletes an item by name
    public String deleteItem(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Item name must be provided.");
        }

        Iterator<InventoryRecord> iterator = inventory.iterator();
        while (iterator.hasNext()) {
            InventoryRecord item = iterator.next();
            if (item.getName().equalsIgnoreCase(name)) {
                iterator.remove();
                logTransaction("Deleted item: " + name);
                sortInventoryByQuantity();
                saveInventoryToFile();
                return "Item deleted successfully.";
            }
        }
        return "Item not found in inventory.";
    }

    // Saves inventory to a file
    public void saveInventoryToFile() {
        ensureDirectoryExists(inventoryFilePath);
        File file = new File(inventoryFilePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (InventoryRecord item : inventory) {
                writer.write(item.getId() + "," + item.getName() + "," + item.getCategory() + "," + item.getQuantity());
                writer.newLine();
            }
            System.out.println("Inventory saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    // Loads inventory from the file
    public void loadInventoryFromFile() {
        inventory.clear();
        File file = new File(inventoryFilePath);
        if (!file.exists()) {
            System.out.println("Inventory file not found.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String category = parts[2];
                    int quantity = Integer.parseInt(parts[3]);
                    inventory.add(new InventoryRecord(id, name, category, quantity));
                }
            }
            InventoryRecord.setIdCounterFromList(inventory);
            System.out.println("Inventory loaded from file.");
        } catch (IOException e) {
            System.out.println("Error loading inventory: " + e.getMessage());
        }
    }

    // Creates an individual inventory file
    private void createInventoryFile(String inventoryName) {
        String fileName = "Data/Inventory/Records/" + inventoryName + ".txt";
        File directory = new File("Data/Inventory/Records/");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Individual file created for inventory: " + inventoryName);
            }
        } catch (IOException e) {
            System.out.println("Error creating individual file for inventory " + inventoryName + ": " + e.getMessage());
        }
    }

    // Updates an individual inventory file
    public void updateInventoryFile(String inventoryName, String inventoryData) {
        String fileName = "Data/Inventory/Records/" + inventoryName + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(inventoryData);
            System.out.println("Inventory file updated for: " + inventoryName);
        } catch (IOException e) {
            System.out.println("Error updating inventory file for " + inventoryName + ": " + e.getMessage());
        }
    }

    // Loads data from an individual inventory file
    public void loadInventoryFromFile(String inventoryName) {
        String fileName = "Data/Inventory/Records/" + inventoryName + ".txt";
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("Inventory file not found for: " + inventoryName);
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Process the inventory data line by line
                System.out.println("Loaded data: " + line);
            }
        } catch (IOException e) {
            System.out.println("Error loading inventory file for " + inventoryName + ": " + e.getMessage());
        }
    }

    // Logs each inventory action
    private void logTransaction(String message) {
        ensureDirectoryExists(logFilePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
            writer.write(new Date() + ": " + message);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error logging transaction: " + e.getMessage());
        }
    }

    // Generates a usage report 
    public void generateUsageReport() throws IOException {
        ensureDirectoryExists(usageReportFilePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(usageReportFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error generating usage report: " + e.getMessage());
        }
    }

    // Displays the inventory in the console
    public void displayInventory() {
        System.out.printf("%-5s %-20s %-20s %-10s\n", "ID", "Item", "Category", "Quantity");
        System.out.println("--------------------------------------------------------------");
        for (InventoryRecord item : inventory) {
            System.out.printf("%-5d %-20s %-20s %-10d\n", item.getId(), item.getName(), item.getCategory(), item.getQuantity());
        }
    }

    // Sorts inventory by quantity in ascending order
    private void sortInventoryByQuantity() {
        inventory.sort(Comparator.comparingInt(InventoryRecord::getQuantity));
    }
}

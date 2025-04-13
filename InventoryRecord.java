package Data_Persistence;

import java.util.List;

public class InventoryRecord {
    private static int idCounter = 1; // starts at 1 and increases for each new item

    private int id;
    private String name;
    private String category;
    private int quantity;

    public InventoryRecord(String name, String category, int quantity) {
        this.id = idCounter++; // assign current value then increment
        this.name = name;
        this.category = category;
        this.quantity = quantity;
    }

    public InventoryRecord(int id, String name, String category, int quantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
    }

    // Static method to set idCounter from a list
    public static void setIdCounterFromList(List<InventoryRecord> records) {
        idCounter = records.stream().mapToInt(InventoryRecord::getId).max().orElse(0) + 1;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

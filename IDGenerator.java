package Data_Persistence;

import java.io.*;

public class IDGenerator {
    private static final String ID_FILE = "Data/Student/Records/lastID.txt";
    private static int lastID = 0;

    // Static block to load the last used ID from a file
    static {
        File file = new File(ID_FILE);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line = br.readLine();
                if (line != null && !line.trim().isEmpty()) {
                    lastID = Integer.parseInt(line.trim());
                }
            } catch (IOException | NumberFormatException e) {
                System.out.println("Error reading last ID: " + e.getMessage());
            }
        }
    }

    // Synchronized to handle multiple calls safely
    public static synchronized int generateID() {
        lastID++;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ID_FILE))) {
            bw.write(Integer.toString(lastID));
        } catch (IOException e) {
            System.out.println("Error writing last ID: " + e.getMessage());
        }
        return lastID;
    }
}

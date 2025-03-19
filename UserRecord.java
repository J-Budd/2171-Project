package Data_Persistence;

// Used GitHub Copilot in VScode to separate the old 2140 code to follow the Package Diagram
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserRecord {
    public static ArrayList<UserRecord> userlist = new ArrayList<>();

    /** The username of the user. */
    protected String username;
    
    /** The password of the user. */
    protected String password;

    /** The role of the user. */
    protected String role;
    
    /** The file associated with the user to store tasks. */
    protected File ufile;

    public UserRecord(String uname, String upass, String urole) {
        this(uname, upass, urole, true);
    }

    private UserRecord(String uname, String upass, String urole, boolean createFile) {
        username = uname;
        password = upass;
        role = urole;
        String fileName = username + ".txt";
        new File("Data/UserData/").mkdirs();
        ufile = new File("Data/UserData/" + fileName);
        
        if (createFile) {
            try {
                if (ufile.createNewFile()) {
                    System.out.println("File created");
                } else {
                    System.out.println("File already exists");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            userlist.add(this);
            saveUsers();
        } else {
            userlist.add(this);
        }
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getRole() {
        return role;
    }

    public File getUFile() {
        return this.ufile;
    }

    public static void loadExistingUsers() {
        Scanner uScan = null;
        try {
            uScan = new Scanner(new File("Data/UserData/Users.txt"));
            while (uScan.hasNext()) {
                String[] nextLine = uScan.nextLine().split(" ");
                if (nextLine.length >= 3) { // Ensure there are at least 3 fields
                    new UserRecord(nextLine[0], nextLine[1], nextLine[2], false);
                }
            }
            uScan.close();
        } catch (IOException e) {
            // Handle exception
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error loading user data: " + e.getMessage());
        }
    }

    public static void saveUsers() {
        File usersFile = new File("Data/UserData/Users.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(usersFile))) {
            for (UserRecord user : UserRecord.userlist) {
                writer.write(user.getUsername() + " " + user.getPassword() + " " + user.getRole());
                writer.newLine();
            }
            System.out.println("Users saved to file: " + usersFile);
        } catch (IOException e) {
            System.out.println("Error saving users to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
}

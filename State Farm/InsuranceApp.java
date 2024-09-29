import java.util.ArrayList;
import java.util.Scanner;
import java.security.MessageDigest;
import java.io.*;
import java.security.NoSuchAlgorithmException;

/**
 * InsuranceApp - State Farm
 */
public class InsuranceApp {
    static ArrayList<User> users = new ArrayList<>();
    static User currentUser = null;
    static final String USER_STRING_DATA_FILE = "users.dat";

    // password hashing
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to save user data
    public static void saveUserData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(USER_STRING_DATA_FILE))) {
            out.writeObject(users);
            System.out.println("User data saved.");
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }

    // Method to load user data
    public static void loadUserData() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(USER_STRING_DATA_FILE))) {
            users = (ArrayList<User>) in.readObject();
            System.out.println("User data loaded.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading user data: " + e.getMessage());
        }
    }

    static class User implements Serializable {
        String userName;
        String password;
        ArrayList<Claim> claims = new ArrayList<>();

        User(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        static class Claim implements Serializable {
            String description;
            double amount;

            Claim(String description, double amount) {
                this.description = description;
                this.amount = amount;
            }
        }
    }

    // Define AutoClaim, HomeClaim, HealthClaim classes
    static class AutoClaim extends User.Claim {
        String licensePlate;

        AutoClaim(String description, double amount, String licensePlate) {
            super(description, amount);
            this.licensePlate = licensePlate;
        }
    }

    static class HomeClaim extends User.Claim {
        String address;

        HomeClaim(String description, double amount, String address) {
            super(description, amount);
            this.address = address;
        }
    }

    static class HealthClaim extends User.Claim {
        String hospitalName;

        HealthClaim(String description, double amount, String hospitalName) {
            super(description, amount);
            this.hospitalName = hospitalName;
        }
    }

    // Method to display insurance tips
    public static void displayInsuranceTips() {
        System.out.println("\nInsurance Safety Tips for Students:");
        System.out.println("1. Always review your coverage to make sure you have what you need.");
        System.out.println("2. Take photos of your belongings and store them securely for claims.");
        System.out.println("3. Drive safely! Install a dashcam for extra security.");
        System.out.println("4. Make sure to understand the terms and conditions for your policy.");
        System.out.println("5. Report any accidents immediately.");
    }

    // Method to submit a claim
    public static void submitClaim() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\nSelect Claim Type: ");
        System.out.println("1. Auto");
        System.out.println("2. Home");
        System.out.println("3. Health");

        int claimType = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter claim description: ");
        String des = sc.nextLine();
        System.out.println("Enter claim amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();

        switch (claimType) {
            case 1 -> {
                System.out.print("Enter license plate: ");
                String licensePlate = sc.nextLine();
                AutoClaim autoClaim = new AutoClaim(des, amount, licensePlate);
                currentUser.claims.add(autoClaim);
            }
            case 2 -> {
                System.out.print("Enter home address: ");
                String address = sc.nextLine();
                HomeClaim homeClaim = new HomeClaim(des, amount, address);
                currentUser.claims.add(homeClaim);
            }
            case 3 -> {
                System.out.print("Enter hospital name: ");
                String hospitalName = sc.nextLine();
                HealthClaim healthClaim = new HealthClaim(des, amount, hospitalName);
                currentUser.claims.add(healthClaim);
            }
            default -> 
            {
                System.out.println("Invalid claim type.");
            }
            
        }
        saveUserData();
        System.out.println("Your claim has been submitted.");
    }

    // Method to view claim history
    public static void viewClaimHistory() {
        System.out.println("View your claim history: ");
        if (currentUser.claims.isEmpty()) {
            System.out.println("No claims are found.");
        } else {
            for (int i = 0; i < currentUser.claims.size(); i++) {
                User.Claim claim = currentUser.claims.get(i);
                System.out.println((i + 1) + ". " + claim.description + " - $" + claim.amount);
            }
        }
    }

    // Method to register a new user
    public static void registerUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nRegister a new Account: ");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        String hashedPassword = hashPassword(password);

        // Check if the username is already taken
        for (User user : users) {
            if (user.userName.equals(username)) {
                System.out.println("Username is already taken. Please try again.");
                return;
            }
        }

        // Create a new user and add to user list
        User newUser = new User(username, hashedPassword);
        users.add(newUser);
        saveUserData();
        System.out.println("Registration successful! You can now log in.");
    }

    // Method to log in to an existing account
    public static boolean login() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nLogin to your account: ");
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        String hashedPassword = hashPassword(password);

        // Find the user and authenticate
        for (User user : users) {
            if (user.userName.equals(username) && user.password.equals(hashedPassword)) {
                currentUser = user;
                System.out.println("Login successful!");
                return true;
            }
        }
        System.out.println("Login failed. Please check your credentials.");
        return false;
    }

    // Method to log out
    public static void logout() {
        currentUser = null;
        System.out.println("You have been logged out.");
    }

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        loadUserData(); // Load user data at the start

        int choice = 0;
        System.out.println("Welcome to State Farm - Insurance Made Easy!");

        do {
            // Check if user is logged in
            if (currentUser == null) {
                System.out.println("\nMenu: ");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Choose an option: ");

                choice = keyboard.nextInt();
                keyboard.nextLine(); // Clear the buffer

                switch (choice) {
                    case 1 -> registerUser();
                    case 2 -> {
                        if (login()) {
                            int loggedIn;
                            do {
                                System.out.println("\nLogged in Menu: ");
                                System.out.println("1. View your Insurance Tips.");
                                System.out.println("2. Submit a claim.");
                                System.out.println("3. View Claim History.");
                                System.out.println("4. Logout.");
                                System.out.println("Choose an option: ");

                                loggedIn = keyboard.nextInt();
                                keyboard.nextLine(); // Clear the buffer

                                switch (loggedIn) {
                                    case 1 -> displayInsuranceTips();
                                    case 2 -> submitClaim();
                                    case 3 -> viewClaimHistory();
                                    case 4 -> logout();
                                    default -> System.out.println("Invalid Option, please try again.");
                                }
                            } while (loggedIn != 4);
                        }
                    }
                    case 3 -> System.out.println("Thank you for using State Farm's Insurance App!");
                    default -> System.out.println("Invalid option, please try again.");
                }
            }
        } while (choice != 3);
        keyboard.close(); // Close the scanner at the end
    }
}

package login;

import user.User;
import admin.Admin;
import customer.Customer;

import java.sql.SQLException;
import java.util.Scanner;

//validation of email/userName and password is required
public class LoginController {

    LoginService loginService;
    Customer customer;
    Admin admin;

    final String RESET = "\u001B[0m";
    final String PURPLE = "\u001b[38;5;171m";
    final String PLEASE = "🙏";
    final String ARROW = "➜";//
    final String EMAIL = "✏\uFE0F";
    final String PASSWORD = "\uD83D\uDD13";


    Scanner scanner;

    public LoginController() {

        this.loginService = new LoginService();
        this.customer = null;
        this.admin = null;
        this.scanner = new Scanner(System.in);
    }
    User user = null;
    public User run() {
        boolean unlogged = true;
        while (unlogged) {
            try {
                System.out.println("\n=== Login ===");
                System.out.println("1. Log in as a customer");
                System.out.println("2. Log in as an admin");
                System.out.println("0. Exit");
                System.out.print(PURPLE + ARROW + " Choose an option " + PLEASE + RESET + "\n");

                String select = scanner.nextLine();

                switch (select) {
                    case "1":

                        System.out.println(PURPLE + ARROW + " Enter email " + EMAIL + RESET + "\n");
                        String email = scanner.nextLine();
                        System.out.println(PURPLE + ARROW + " Enter password " + PASSWORD + RESET + "\n");
                        String password = scanner.nextLine();
                        user = loginService.loginAsCustomer(email, password);//из этого метода customer не сохраняется в метод run
                        unlogged = false;
                        return user;


                    case "2":
                        System.out.println(PURPLE + ARROW + " Enter name " + EMAIL + RESET + "\n");
                        String userName = scanner.nextLine();
                        System.out.println(PURPLE + ARROW + " Enter password " + PASSWORD + RESET + "\n");
                        String adminPassword = scanner.nextLine();
                        user = loginService.loginAsAdmin(userName, adminPassword);
                        unlogged = false;
                        return user;

                    case "0":
                        System.out.println("Exit the program...");
                        return null;
                    default:
                        System.out.println("Incorrect option. Try again.");
                }
            } catch (SQLException e) {
                // Handling a database error
                System.out.println("An error occurred during database call: " + e.getMessage());
            } catch (Exception e) {
                // Handling other error
                System.out.println("An unexpected error occurred: " + e.getMessage());
                scanner.nextLine(); // Clear the scanner buffer in case of a wrong input
            }
        }
        return user;
    }
}

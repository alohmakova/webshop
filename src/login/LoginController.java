package login;

import util.User;
import admin.Admin;
import customer.Customer;

import java.sql.SQLException;
import java.util.Scanner;

//validation of email/userName and password is required
public class LoginController {

    LoginService loginService;
    Customer customer;
    //Admin admin;


    Scanner scanner;

    public LoginController() {

        this.loginService = new LoginService();
        this.customer = null;
        //this.admin = null;
        this.scanner = new Scanner(System.in);
    }

    public Customer run() {
        boolean unlogged = true;
        while (unlogged) {
            try {
                System.out.println("\n=== Login ===");
                System.out.println("1. Log in as a customer");
                System.out.println("2. Log in as an admin");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");

                String select = scanner.nextLine();

                switch (select) {
                    case "1":
                        System.out.println("Enter email: ");
                        String email = scanner.nextLine();
                        System.out.println("Enter password: ");
                        String password = scanner.nextLine();
                        Customer customer = loginService.loginAsCustomer(email, password);//из этого метода customer не сохраняется в метод run
                            unlogged = false;
                            return customer;

                    case "2":
                        System.out.println("Enter name: ");
                        String userName = scanner.nextLine();
                        System.out.println("Enter password: ");
                        String adminPassword = scanner.nextLine();
                        loginService.loginAsAdmin(userName, adminPassword);
                        unlogged = false;
                        return null;

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
        return customer;
    }
}

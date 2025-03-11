package login;

import user.User;
import admin.Admin;
import customer.Customer;
import util.TextStyle;
import java.sql.SQLException;
import java.util.Scanner;
import static util.TextStyle.*;

//validation of email/userName and password is required
public class LoginController {

    LoginService loginService;
    Customer customer;
    Admin admin;
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
                System.out.print(OPTION.getStyle());

                String select = scanner.nextLine();
                String askPassword = PURPLE.getStyle() + ARROW.getStyle() + " Enter password " + PASSWORD.getStyle() + RESET.getStyle() + "\n";

                switch (select) {
                    case "1":

                        String email = loginService.askEmail();
                        System.out.println(askPassword);
                        String password = scanner.nextLine();
                        user = loginService.loginAsCustomer(email, password);//из этого метода customer не сохраняЛся в метод run поэтому был NullPointerException
                        unlogged = false;
                        return user;


                    case "2":
                        System.out.println(PURPLE.getStyle() + ARROW.getStyle() + " Enter name " + EMAIL.getStyle() + RESET.getStyle() + "\n");
                        String userName = scanner.nextLine();
                        System.out.println(askPassword);
                        String adminPassword = scanner.nextLine();
                        user = loginService.loginAsAdmin(userName, adminPassword);
                        unlogged = false;
                        return user;

                    case "0":
                        System.out.println(BYE.getStyle());
                        return null;
                    default:
                        System.out.println(WRONG_OPTION.getStyle());
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

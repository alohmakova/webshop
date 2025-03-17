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

                switch (select) {
                    case "1":
                        String email = loginService.askEmail();
                        user = loginService.loginAsCustomer(email);//password is asked inside the method loginAsCustomer() only if such user exists in the system
                        unlogged = user == null;//start login() method again if the user== null
                        //unlogged = (user != null) ? false : true;
                        break;

                    case "2":
                        String userName = loginService.askUserName();
                        user = loginService.loginAsAdmin(userName);//password is asked inside the method loginAsCustomer() only if such user exists in the system
                        unlogged = user == null;//start login() method again if the user== null
                        break;

                    case "0":
                        unlogged = false;//finish login() method, user== null
                        System.out.println(BYE.getStyle());
                        break;
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

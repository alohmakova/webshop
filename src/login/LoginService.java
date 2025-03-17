package login;

import admin.Admin;
import admin.AdminRepository;
import customer.*;
import customer.CustomerRepository;
import user.User;
import util.BaseLogger;

import java.sql.SQLException;

import static order.OrderController.scanner;
import static util.TextStyle.*;

public class LoginService extends BaseLogger{

    CustomerRepository customerRepository;
    AdminRepository adminRepository;
    String wrongPass = NO.getStyle() + " Wrong password " + NO.getStyle() + RESET.getStyle() + "\n";

    public LoginService() {
        this.customerRepository = new CustomerRepository();
        this.adminRepository = new AdminRepository();
    }

    public Customer loginAsCustomer(String email) throws SQLException {

        Customer customer = customerRepository.getCustomerByEmail(email);

        if(customer == null){
            System.out.println(BLUE.getStyle() + ARROW.getStyle() + " Customer not found " + NOTFOUND.getStyle() + RESET.getStyle() + "\n");
            logger.warning("Customer not found. The loginController.run() method will be restarted");
            return null;
        }
        String password = askPassword();
        logger.fine("Entered password is at least 6 characters long and is valid");
        if(customer.getPassword().equals(password)){
            logger.info("Entered password is correct");
            System.out.println(CONGRATULATIONS.getStyle() + BOLD.getStyle() + GREEN.getStyle() + " Welcome, " + customer.getName() + "! " + CONFETTI.getStyle() + "\n" + RESET.getStyle() + "\n");
            return customer;
        }
        else{
            System.out.println(wrongPass);
            return null;
        }

    }

    public Admin loginAsAdmin(String name) throws SQLException {
        Admin admin = adminRepository.getAdminByUserName(name);

        if(admin == null){
            System.out.println(BLUE.getStyle() + ARROW.getStyle() + " User not found " + NOTFOUND.getStyle() + RESET.getStyle() + "\n");
            logger.warning("User not found. The loginController.run() method will be restarted");
            return null;
        }
        String password = askPassword();
        logger.fine("Entered password is at least 6 characters long and is valid");
        if(admin.getPassword().equals(password)){
            logger.info("Entered password is correct");
            System.out.println(CONGRATULATIONS.getStyle() + BOLD.getStyle() + GREEN.getStyle() + " Welcome, " + admin.getUserName() + "! " + CONFETTI.getStyle() + "\n" + RESET.getStyle() + "\n");
            return admin;
        }
        else{
            System.out.println(wrongPass);
            return null;
        }

    }

    public String askUserName() {
        System.out.println(PURPLE.getStyle() + ARROW.getStyle() + " Enter name " + EMAIL.getStyle() + RESET.getStyle() + "\n");
        logger.fine("The user enters user name ");
        String userName = scanner.nextLine();
        if (userName.length() < 3) {
            logger.warning("Entered user name is too short: '" + userName + "'. The name must be at least 3 characters long");
            System.out.println("Entered user name is too short. The name must be at least 3 characters long. Try again");
            return askUserName();

        }
        logger.info("Entered user name is at least 3 characters long and is valid");
        return userName;
    }

    public String askEmail() {
        System.out.println(PURPLE.getStyle() + ARROW.getStyle() + " Enter email " + EMAIL.getStyle() + RESET.getStyle() + "\n");
        logger.fine("The user enters e-mail ");
        String email = scanner.nextLine();
        if (!email.matches("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+$")) {
            System.out.println("Incorrect email format. Try again");
            logger.warning("The email format was incorrect: '" + email + "'. The user need to re-enter the email");
            return askEmail();
        }
        logger.info("Entered email matches the regular expression pattern and is valid");
        return email;

    }

    public String askPassword() {
        System.out.println(PURPLE.getStyle() + ARROW.getStyle() + " Enter password " + PASSWORD.getStyle() + RESET.getStyle() + "\n");
        logger.fine("The user enters password ");
        String password = scanner.nextLine();
        if (password.length() < 6) {
            logger.warning("Entered password is too short: '" + password + "'. The password must be at least 6 characters long");
            System.out.println("Entered password is too short: '" + password + "'. The password must be at least 6 characters long. Try again");
        }
        return password;
    }

}

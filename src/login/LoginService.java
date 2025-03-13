package login;

import admin.Admin;
import admin.AdminRepository;
import customer.*;
import customer.CustomerRepository;
import order.AdminOrderInteraction;
import util.BaseLogger;

import java.sql.SQLException;
import java.util.logging.Logger;

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

    public Customer loginAsCustomer(String email, String password) throws SQLException {

        Customer customer = customerRepository.getCustomerByEmail(email);

        if(customer == null){
            System.out.println(BLUE.getStyle() + ARROW.getStyle() + " Customer not found " + NOTFOUND.getStyle() + RESET.getStyle() + "\n");
            return null;
        }
        else if(customer.getPassword().equals(password)){
            System.out.println(CONGRATULATIONS.getStyle() + BOLD.getStyle() + GREEN.getStyle() + " Welcome, " + customer.getName() + "! " + CONFETTI.getStyle() + "\n" + RESET.getStyle() + "\n");
            return customer;
        }
        else{
            System.err.println(wrongPass);
        }
        return null;
    }

    public Admin loginAsAdmin(String name, String password) throws SQLException {
        Admin admin = adminRepository.getAdminByUserName(name);

        if(admin == null){
            System.out.println(BLUE.getStyle() + ARROW.getStyle() + " User not found " + NOTFOUND.getStyle() + RESET.getStyle() + "\n");
            return null;
        }
        else if(admin.getPassword().equals(password)){
            System.out.println(CONGRATULATIONS.getStyle() + BOLD.getStyle() + GREEN.getStyle() + " Welcome, " + admin.getUserName() + "! " + CONFETTI.getStyle() + "\n" + RESET.getStyle() + "\n");
            return admin;
        }
        else{
            System.out.println(wrongPass);
        }
        return null;

    }

    public String askEmail() {
        System.out.println(PURPLE.getStyle() + ARROW.getStyle() + " Enter email " + EMAIL.getStyle() + RESET.getStyle() + "\n");
        logger.fine("The user enters e-mail ");
        String email = scanner.nextLine();
        if (email.matches("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+$")) {
            logger.info("The entered email matches the regular expression pattern and is valid");
            return email;
        }
        System.out.println("Incorrect email format. Try again");
        logger.warning("The email format was incorrect " + email + ", the user need to re-enter the email");
        return askEmail();

    }

}

package login;

import admin.Admin;
import admin.AdminRepository;
import customer.*;
import customer.CustomerRepository;
import java.sql.SQLException;
import static util.TextStyle.*;

public class LoginService {

    CustomerRepository customerRepository;
    AdminRepository adminRepository;
    String wrongPass = NO.getStyle() + " Wrong password " + NO.getStyle() + "\n";

    public LoginService() {
        this.customerRepository = new CustomerRepository();
        this.adminRepository = new AdminRepository();
    }

    public Customer loginAsCustomer(String email, String password) throws SQLException {

        Customer customer = customerRepository.getCustomerByEmail(email);

        if(customer == null){
            System.out.println(BLUE.getStyle() + ARROW.getStyle() + " Customer not found " + NOTFOUND.getStyle() + "\n");
        }
        else if(customer.getPassword().equals(password)){
            System.out.println(CONGRATULATIONS.getStyle() + BOLD.getStyle() + GREEN.getStyle() + " Welcome, " + customer.getName() + "! " + CONFETTI.getStyle() + "\n" + RESET.getStyle());
            return customer;
        }
        else{
            System.out.println(wrongPass);
        }
        return null;
    }

    public Admin loginAsAdmin(String name, String password) throws SQLException {
        Admin admin = adminRepository.getAdminByUserName(name);

        if(admin == null){
            System.out.println(BLUE.getStyle() + ARROW.getStyle() + " User not found " + NOTFOUND.getStyle() + "\n");
        }
        else if(admin.getPassword().equals(password)){
            System.out.println(CONGRATULATIONS.getStyle() + BOLD.getStyle() + GREEN.getStyle() + " Welcome, " + admin.getUserName() + "! " + CONFETTI.getStyle() + "\n" + RESET.getStyle());
            return admin;
        }
        else{
            System.out.println(wrongPass);
        }
        return null;

    }

}

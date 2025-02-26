package login;

import admin.Admin;
import admin.AdminRepository;
import customer.*;
import customer.CustomerRepository;

import java.sql.SQLException;

public class LoginService {

    CustomerRepository customerRepository;
    AdminRepository adminRepository;
    // ANSI colours and formatting
    final String GREEN = "\u001B[32m";
    final String BOLD = "\u001B[1m";
    final String RESET = "\u001B[0m";

    // Unicode symboler
    final String CONGRATULATIONS = "🥳";
    final String CONFETTI = "🎉";

    public LoginService() {
        this.customerRepository = new CustomerRepository();
        this.adminRepository = new AdminRepository();
    }

    public Customer loginAsCustomer(String email, String password) throws SQLException {

        Customer customer = customerRepository.getCustomerByEmail(email);

        if(customer == null){
            System.out.println("No customer found");
        }
        else if(customer.getPassword().equals(password)){
            System.out.println(CONGRATULATIONS + BOLD + GREEN + " Welcome, " + customer.getName() + "! " + CONFETTI + "\n" + RESET);
            return customer;
        }
        else{
            System.out.println("Wrong password");
        }
        return null;
    }

    public Admin loginAsAdmin(String name, String password) throws SQLException {
        Admin admin = adminRepository.getAdminByUserName(name);

        if(admin == null){
            System.out.println("No user found");
        }
        else if(admin.getPassword().equals(password)){
            System.out.println("Congrats you've logged in");
            return admin;
        }
        else{
            System.out.println("Wrong password");
        }
        return null;

    }

}

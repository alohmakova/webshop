package customer;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Applikationen ska struktureras i tre huvudsakliga lager:
 * Det är Service-lager som hanterar affärslogik
 * Nämligen service-klass för kundhantering
 * Innehåller affärslogik mellan controller (meny) och repository (database)
 * class CustomerService contains methods to implement the menu, which is located in the class CustomerController
 */
public class CustomerService {

    // Repository som hanterar alla databasanrop
    CustomerRepository customerRepository;

    /**
     * Konstruktor för CustomerService
     * Initierar repository-lagret
     */
    public CustomerService() {
        this.customerRepository = new CustomerRepository();
    }

    /**
     * Hämtar och visar alla kunder från databasen
     * Service-lagret kan här:
     * - Formatera utskriften
     * - Lägga till affärslogik (t.ex. filtrera bort inaktiva kunder)
     * - Hantera specialfall (t.ex. om listan är tom)
     *
     * @throws SQLException vid problem med databasanrop
     */
    public void showAllUsers() throws SQLException {
        // Get all customers from the repository
        ArrayList<Customer> customers = customerRepository.getAllCustomers();

        // Check if we have any customers to show
        if (customers.isEmpty()) {
            System.out.println("Inga kunder hittades.");
            return;
        }

        // Print all customers with clear formatting
        System.out.println("\n=== Kundlista ===");
        //I'm replacing the traditional for-each loop with a lambda
        customers.forEach(customer ->
        {
            System.out.println("ID: " + customer.getCustomerId());
            System.out.println("Namn: " + customer.getFirstName() + " " + customer.getLastName());
            System.out.println("Email: " + customer.getEmail());
            System.out.println("-----------------");
        });
    }


}

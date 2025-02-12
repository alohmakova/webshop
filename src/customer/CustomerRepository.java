package customer;

import java.sql.*;
import java.util.ArrayList;
import util.DatabaseConnection;

/**
 * Applikationen ska struktureras i tre huvudsakliga lager:
 * Det är Repository-lager för databasinteraktion via JDBC
 * Nämligen repository-klass för kundhantering
 * Hanterar alla databasoperationer för Customer-entiteten
 */
public class CustomerRepository {

    /**
     * Hämtar alla kunder från databasen
     * Skapar en ny anslutning, hämtar data och stänger anslutning automatiskt
     *
     * @return ArrayList med alla kunder
     * @throws SQLException vid problem med databasanrop
     */
    public ArrayList<Customer> getAllCustomers() throws SQLException {
        ArrayList<Customer> customers = new ArrayList<>();

        // try-with-resources stänger automatiskt Connection, Statement och ResultSet
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customers")) {

            // Loopa igenom alla rader från databasen
            while (rs.next()) {
                // Skapa ett nytt Customer-objekt från varje databasrad
                Customer customer = new Customer(
                        rs.getInt("customer_id"),     // Hämta ID från customer_id kolumnen
                        rs.getString("first_name"),   // Hämta förnamn
                        rs.getString("last_name"),    // Hämta efternamn
                        rs.getString("email")         // Hämta email
                );
                customers.add(customer);
            }
        }
        return customers;
    }

    //I am changing Statement to PreparedStatement
    /*public int getCustomerIdByEmail(String email) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT customer_id FROM customers WHERE email = '" + email + "'")) {
            if (rs.next()) {
                int customerId = rs.getInt("customer_id");
                return customerId;

            }
        }
        //System.out.println(-1);
        return -1;
    }*/
    public int getCustomerIdByEmail(String email) throws SQLException {
        //it is impossible to get multiple IDs with the same email, because email is unique in database

        String query = "SELECT customer_id FROM customers WHERE email = ?";


        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email); // Set the query parameter (?)

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int customerId = rs.getInt("customer_id");
                    return customerId;

                }
            }
        }
        return -1;
    }
}
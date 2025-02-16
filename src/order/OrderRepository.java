package order;

import java.sql.*;
import java.util.ArrayList;
import util.DatabaseConnection;


public class OrderRepository {

    //show the history of orders
    /*public ArrayList<Order> getAllOrdersbyCustomerId(int customerId) throws SQLException {
        ArrayList<Order> ordersByCustomerId = new ArrayList<>();


        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM orders WHERE customer_id = " + customerId)) {

            // Loopa igenom alla rader från databasen
            while (rs.next()) {
                // Skapa ett nytt Order-objekt från varje databasrad
                Order order = new Order(
                        rs.getInt("order_id"),
                        rs.getInt("customer_id"),
                        rs.getString("order_date"),
                        rs.getDouble("total_amount")
                );
                ordersByCustomerId.add(order);
            }
        }
        return ordersByCustomerId;
    }*/
    //I am changing Statement to PreparedStatement
    public ArrayList<Order> getAllOrdersbyCustomerId(int customerId) throws SQLException {
        ArrayList<Order> ordersByCustomerId = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, customerId); // Set the query parameter (?)

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order(
                            rs.getInt("order_id"),
                            rs.getInt("customer_id"),
                            rs.getString("order_date")
                    );
                    ordersByCustomerId.add(order);
                }
            }
        }
        return ordersByCustomerId;
    }


        public int getMaxOrderId() throws SQLException {
             try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MAX(order_id) FROM orders")) {
                 if (rs.next()) {
                     return rs.getInt(1);
                 }
        }
        return -1;
    }

    //send the order to the DB
    public void addOrder(Order order) throws SQLException {
        String query = "INSERT INTO orders (order_id, customer_id, order_date) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setInt(1, order.getOrderNumber());
            pstmt.setInt(2, order.getCustomerId());
            pstmt.setString(3, order.getOrderDate());
            pstmt.executeUpdate();
        }
    }

}

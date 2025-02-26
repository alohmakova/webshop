package order;

import java.sql.*;
import java.util.ArrayList;

import product.Product;
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
        String query = "select orders.order_id, orders.customer_id, orders.order_date, products.name, products.price, orders_products.quantity from orders " +
                "join orders_products on orders.order_id = orders_products.order_id " +
                "join products on orders_products.product_id = products.product_id " +
                "where customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, customerId); // Set the query parameter (?)

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order(
                            rs.getInt("order_id"),
                            rs.getInt("customer_id"),
                            rs.getString("order_date"),
                            rs.getString("name"),
                            rs.getInt("quantity"),
                            rs.getDouble("price")*rs.getInt("quantity")

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

    //save new order in the database in the orders table
    public void addOrder(Order order) throws SQLException {
        String query = "INSERT INTO orders (customer_id, order_date) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, order.getCustomerId());
            pstmt.setString(2, order.getOrderDate());
            pstmt.executeUpdate();
        }
    }

    //save info about new order in the database in the orders_products table
    public void insertIntoOrdersProducts(Order order, Product product) throws SQLException {
        String query = "INSERT INTO orders_products (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, order.orderNumber);
            pstmt.setInt(2, product.getProductId());
            pstmt.setInt(3, order.quantity);
            pstmt.setDouble(4, product.getProductPrice());
            pstmt.executeUpdate();

        }
    }
}

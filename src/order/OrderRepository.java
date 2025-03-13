package order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

    /**
     the query gives me orders with duplicated id's, because if the order has been changed, the table orders_products will have rows with duplicated id's
     HashSet<Order> gives me the possibility to display only unique records without duplicated id's to the customer
     **/
    public ArrayList<Order> getAllOrdersByCustomerId(int customerId) throws SQLException {
        ArrayList<Order> ordersByCustomerId = new ArrayList<>();

        String query = "SELECT orders.order_id, orders.customer_id, orders.order_date, products.name, products.price, orders_products.quantity " +
                "FROM orders " +
                "LEFT JOIN orders_products ON orders.order_id = orders_products.order_id " +
                "LEFT JOIN products ON orders_products.product_id = products.product_id " +
                "WHERE customer_id = ?";


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
                            rs.getDouble("price") * rs.getInt("quantity")

                    );
                    ordersByCustomerId.add(order);
                }
            }
        }
        return ordersByCustomerId;
    }

    /**
     the query gives me orders with duplicated id's, because if the order has been changed, the table orders_products will have rows with duplicated id's
     HashSet<Order> gives me the possibility to display only unique records without duplicated id's to the customer
     **/

    public ArrayList<Order> getLimitedOrdersByCustomerId(int customerId) throws SQLException {
        ArrayList<Order> ordersByCustomerId = new ArrayList<>();
        String query = "SELECT orders.order_id, orders.customer_id, orders.order_date, products.name, products.price, orders_products.quantity " +
                "FROM orders " +
                "LEFT JOIN orders_products ON orders.order_id = orders_products.order_id " +
                "LEFT JOIN products ON orders_products.product_id = products.product_id " +
                "WHERE customer_id = ? ORDER BY orders.order_date DESC LIMIT 10";


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
                            rs.getDouble("price") * rs.getInt("quantity")

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
    public void saveOrder(Order order) throws SQLException {
        String query = "INSERT INTO orders (customer_id, order_date) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, order.getCustomerId());
            pstmt.setString(2, order.getOrderDate());
            pstmt.executeUpdate();
        }
    }

    //save info about new order in the database in the orders_products table
    public void insertIntoOrdersProducts(Order order, Order order1, Product product) throws SQLException {
        String query = "INSERT INTO orders_products (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, order1.getOrderId());
            pstmt.setInt(2, product.getProductId());
            pstmt.setInt(3, order.getQuantity());
            //without this rounding, the database gets a number like this 906.9900000000001 instead of 906.99
            pstmt.setDouble(4, BigDecimal.valueOf(order.getTotalAmount() / order.getQuantity())
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue());//it was product.getProductPrice() before
            pstmt.executeUpdate();

        }
    }

    public void deleteOrder(int orderId) {
        String query1 = "DELETE FROM orders_products WHERE order_id = ?";
        String query2 = "DELETE FROM orders WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt1 = conn.prepareStatement(query1);
             PreparedStatement pstmt2 = conn.prepareStatement(query2)) {

            pstmt1.setInt(1, orderId);
            pstmt1.executeUpdate();

            pstmt2.setInt(1, orderId);
            pstmt2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveChangedOrder(Order order, Product product, int quantity) {
/**
 I changed the database so that in the orders_products table the order_id column must be unique to avoid duplicates when changing an order
 **/
        String query1 = "UPDATE orders_products SET quantity = ?, unit_price = ? WHERE order_id = ?";
        String query2 = "UPDATE orders SET order_date = ? WHERE order_id = ?";


        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt1 = conn.prepareStatement(query1);
             PreparedStatement pstmt2 = conn.prepareStatement(query2))
        {

            pstmt1.setInt(1, quantity);
            pstmt1.setDouble(2, product.getProductPrice());//I have to take it from the product, not from the old order, because it could be different
            pstmt1.setInt(3, order.getOrderId());
            pstmt1.executeUpdate();

            pstmt2.setString(1, order.getOrderDate());
            pstmt2.setInt(2, order.getOrderId());
            pstmt2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**public ArrayList<Order> getHistoryOfOrderChanges(int orderId) {
        ArrayList<Order> ordersById = new ArrayList<Order>();
        String query = "select orders.order_id, orders.customer_id, orders.order_date, order_history.old_date, products.name, products.price, orders_products.quantity from orders \n" +
                "left join orders_products on orders.order_id = orders_products.order_id\n" +
                "left join order_history on order_history.order_id = orders.order_id  \n" +
                "left join products on orders_products.product_id = products.product_id \n" +
                "where orders.order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, orderId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order(
                            rs.getInt("order_id"),
                            rs.getInt("customer_id"),
                            rs.getString("order_date"),
                            rs.getString("old_date"),
                            rs.getString("name"),
                            rs.getInt("quantity"),
                            rs.getDouble("price") * rs.getInt("quantity")

                    );
                    ordersById.add(order);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ordersById;
    }*/

    public Order getOrderById(int orderId) throws SQLException {
        Order order = null;
        String query = "select orders.order_id, orders.customer_id, orders.order_date, products.name, products.price, orders_products.quantity from orders " +
                "left join orders_products on orders.order_id = orders_products.order_id " +
                "left join products on orders_products.product_id = products.product_id " +
                "where orders.order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, orderId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                        order = new Order(
                        rs.getInt("order_id"),
                        rs.getInt("customer_id"),
                        rs.getString("order_date"),
                                rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price") * rs.getInt("quantity"));

            } else {
                throw new SQLException("Order with id " + orderId + " not found");
            }


        }
        return order;
    }
}

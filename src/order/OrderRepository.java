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
    public ArrayList<Order> getAllOrdersByCustomerId(int customerId) throws SQLException {
        ArrayList<Order> ordersByCustomerId = new ArrayList<>();
        String query = "SELECT orders.order_id, orders.customer_id, orders.order_date, orders.order_number, products.name, products.price, orders_products.quantity \n" +
                "FROM orders\n" +
                "LEFT JOIN orders_products ON orders.order_id = orders_products.order_id\n" +
                "LEFT JOIN products ON orders_products.product_id = products.product_id \n" +
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

    public ArrayList<Order> getLimitedOrdersByCustomerId(int customerId) throws SQLException {
        ArrayList<Order> ordersByCustomerId = new ArrayList<Order>();
        String query = "SELECT orders.order_id, orders.customer_id, orders.order_date, orders.order_number, products.name, products.price, orders_products.quantity " +
                "FROM orders " +
                "LEFT JOIN orders_products ON orders.order_id = orders_products.order_id" +
                "LEFT JOIN products ON orders_products.product_id = products.product_id" +
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
        String query = "INSERT INTO orders (customer_id, order_date, order_number) VALUES (?, ?, ?)";
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
            pstmt.setInt(1, order.getOrderId());
            pstmt.setInt(2, product.getProductId());
            pstmt.setInt(3, order.getQuantity());
            pstmt.setDouble(4, order.getTotalAmount() / order.getQuantity());//product.getProductPrice()
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

    public void saveChangedOrder(int customerId, Order order, Product product, int quantity) {
        /**When changing/updating an order, I don't want to change the data in the orders_products,
         **I create a new record so that I can track the history of order changes*/
        String query1 = "INSERT INTO orders_products (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        String query2 = "INSERT INTO orders (order_id, customer_id, order_date, order_number) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt1 = conn.prepareStatement(query1);
             PreparedStatement pstmt2 = conn.prepareStatement(query2)) {

            pstmt1.setInt(1, order.getOrderId());
            pstmt1.setInt(2, product.getProductId());
            pstmt1.setInt(3, quantity);
            pstmt1.setDouble(4, product.getProductPrice());
            pstmt1.executeUpdate();

            pstmt2.setInt(1, customerId);
            pstmt2.setString(2, order.getOrderDate());//generate current time and date
            pstmt2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Order> getHistoryOfOrderChanges(int orderId) {
        ArrayList<Order> ordersById = new ArrayList<Order>();
        String query = "select orders.order_id, orders.customer_id, orders.order_date, orders.order_number, products.name, products.price, orders_products.quantity from orders " +
                "join orders_products on orders.order_id = orders_products.order_id " +
                "join products on orders_products.product_id = products.product_id " +
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
    }

    public Order getOrderById(int orderId) throws SQLException {
        Order order = null;
        String query = "select orders.order_id, orders.customer_id, orders.order_date, orders.order_number, products.name, products.price, orders_products.quantity from orders " +
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
                return null;
            }


        }
        return order;
    }
}

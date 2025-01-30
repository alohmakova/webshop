import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class OrderRepository {
    public ArrayList<Order> getAllOrdersbyCustomerId(int customerId) throws SQLException {
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
                        rs.getDate("order_date"),
                        rs.getDouble("total_amount")
                );
                ordersByCustomerId.add(order);
            }
        }
        return ordersByCustomerId;
    }
}

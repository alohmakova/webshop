import java.sql.*;
import order.OrderController;
import customer.CustomerController;

public class Main {
    public static void main(String[] args) throws SQLException {

        OrderController orderController = new OrderController();
        orderController.run();

        CustomerController customerController = new CustomerController();
        customerController.run();

    }
}
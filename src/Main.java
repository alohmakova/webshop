import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import customer.CustomerRepository;
import login.LoginController;
import order.OrderController;
import customer.CustomerController;

public class Main {
    public static void main(String[] args) throws SQLException {

        LoginController loginController = new LoginController();
        //loginController.run();

        OrderController orderController = new OrderController();
        orderController.run();

        //CustomerController customerController = new CustomerController();
        //customerController.run();

    }
}
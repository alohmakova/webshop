import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import customer.CustomerRepository;
import login.LoginController;
import customer.CustomerController;
import order.OrderLoginController;

public class Main {
    public static void main(String[] args) throws SQLException {

        LoginController loginController = new LoginController();
        //loginController.run();

        OrderLoginController orderLoginController = new OrderLoginController();
        orderLoginController.run();

        //CustomerController customerController = new CustomerController();
        //customerController.run();

    }
}
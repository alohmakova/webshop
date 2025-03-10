import java.sql.*;

import login.LoginController;
import order.OrderLogin;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        LoginController loginController = new LoginController();
        //loginController.run();

        OrderLogin orderLogin = new OrderLogin();
        orderLogin.run();

        //CustomerController customerController = new CustomerController();
        //customerController.run();

    }
}
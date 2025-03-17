package order;

import admin.Admin;
import customer.Customer;
import login.LoginController;
import user.User;
import util.BaseLogger;

import java.sql.SQLException;
import java.util.logging.Level;

/**
 Depending on whether the user logs in as a customer or as an admin, they can use different functions
 */

public class OrderLogin extends BaseLogger {

    LoginController loginController;
    OrderController orderController;

    User user;


    public OrderLogin() {
        this.loginController = new LoginController();
    }

    public void run() throws SQLException {
            //do nothing if user== null, it means that the program will be finished
            user = loginController.run();
            if (user instanceof Customer customer) {
                orderController = new CustomerOrderInteraction();
                logger.info("The user is logged in as a customer");
                orderController.selectOrderOption(customer);
            } else if (user instanceof Admin admin) {
                orderController = new AdminOrderInteraction();
                logger.info("The user is logged in as an administrator");
                orderController.selectOrderOption(admin);
            }
    }
}
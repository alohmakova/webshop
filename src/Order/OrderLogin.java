package order;

import admin.Admin;
import customer.Customer;
import login.LoginController;
import user.User;

import java.sql.SQLException;

/**
 Depending on whether the user logs in as a customer or as an admin, they can use different functions
 */

public class OrderLogin {

    LoginController loginController;
    OrderController orderController;

    User user;


    public OrderLogin() {
        this.loginController = new LoginController();
    }

    /**
     Exception in thread "main" java.lang.NullPointerException: Cannot invoke "order.OrderController.selectOrderOption(user.User)" because "this.orderController" is nul
     **/

    public void run() throws SQLException {
            //do nothing if user== null, it means that the program will be finished
            user = loginController.run();
        if (user instanceof Customer customer) {
            orderController = new CustomerOrderInteraction();
            orderController.selectOrderOption(customer);
        } else if (user instanceof Admin admin) {
            orderController = new AdminOrderInteraction();
            orderController.selectOrderOption(admin);
    }
    }
}
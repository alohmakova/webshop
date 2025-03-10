package order;

import admin.Admin;
import customer.Customer;
import login.LoginController;
import user.User;

import java.sql.SQLException;

/**
 Depending on whether the user logs in as a customer or as an admin, they can use different functions
 */

public class OrderLoginController {

    LoginController loginController;
    OrderWorkFlow orderWorkFlow;

    User user;


    public OrderLoginController() {
        this.loginController = new LoginController();
        this.orderWorkFlow = new CustomerOrderController();

    }

    public void run() throws SQLException {
        user = loginController.run();
        if (user == null) {
            //if the user is not found, the program will be terminated
        }else if (user instanceof Customer customer) {
            orderWorkFlow.selectOrderOption(customer);
        }else if (user instanceof Admin admin) {
            orderWorkFlow.selectOrderOption(admin);
        }
    }
}

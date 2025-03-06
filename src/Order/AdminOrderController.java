package order;

import admin.Admin;
import customer.Customer;
import user.User;

public class AdminOrderController implements OrderManager{

    @Override
    public void performActions(User user) {

        Admin admin = (Admin)user;

    }
}

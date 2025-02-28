package order;

import admin.Admin;
import customer.Customer;
import user.User;

public class AdminOrders implements OrderManager{

    @Override
    public void performActions(User user) {

        Admin admin = (Admin)user;

    }
}

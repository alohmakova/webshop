package order;

import admin.Admin;
import customer.Customer;
import customer.CustomerRepository;
import login.LoginService;
import user.User;

import java.sql.SQLException;

import static util.TextStyle.*;

public class AdminOrderController implements OrderWorkFlow {

    LoginService loginService = new LoginService();
    CustomerRepository customerRepository = new CustomerRepository();
    Admin admin;
    Customer customer = selectCustomer();

    @Override
    public void selectOrderOption(User user) throws SQLException {

        admin = (Admin)user;
        while (true) {
            try {

                System.out.println("\n=== Orders: back office ===\n" +
                        "1. Create order for the customer\n" +//it could be a fast order: when I do not show all products and categories - andmin only enter them manually or using search
                        "2. Delete order for the customer\n" +
                        "3. Change order for the customer\n" +
                        "4. Show order history for the customer\n" +
                        "0. Exit\n" +
                        OPTION.getStyle());

                String select = scanner.nextLine();

                switch (select) {
                    case "1":
                        createOrder(customer);
                        break;
                    case "2":
                        orderService.showAllCustomersOrdersByID(customer.getCustomerId());
                        orderService.deleteOrder(orderService.chooseOrder(scanner, customer.getCustomerId()));
                        orderService.showAllCustomersOrdersByID(customer.getCustomerId());
                        break;
                    case "3":

                        break;
                    case "4":
                        //good to add search by order history, e.g. by date, by product, by price, by category, by id
                        orderService.showAllCustomersOrdersByID(customer.getCustomerId());
                        break;
                    case "0":
                        System.out.println(BYE.getStyle());
                        return;
                    default:
                        System.out.println(WRONG_OPTION.getStyle());
                        System.out.println("Please, provide the number 0-4 instead of " + "\"" + select + "\"\n");
                }
            } catch (Exception e) {
                // Handle other errors (e.g. incorrect input)
                System.out.println("An unexpected error occurred: " + e.getMessage());
                scanner.nextLine();
            }
        }

    }

    private Customer selectCustomer() {
        try {
            System.out.println("Select a customer\n");
            return customerRepository.getCustomerByEmail(loginService.askEmail());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}

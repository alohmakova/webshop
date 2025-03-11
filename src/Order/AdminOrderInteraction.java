package order;

import admin.Admin;
import customer.Customer;
import customer.CustomerRepository;
import login.LoginService;
import product.Product;
import user.User;

import java.sql.SQLException;

import static util.TextStyle.*;

public class AdminOrderInteraction implements OrderController {

    LoginService loginService = new LoginService();
    CustomerRepository customerRepository = new CustomerRepository();
    Admin admin;
    Customer customer;

    @Override
    public void selectOrderOption(User user) throws SQLException {

        admin = (Admin) user;
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
                //везде нужна валидация тогго, что пользователь с таким емейлом существует
                //An unexpected error occurred: Cannot invoke "customer.Customer.getCustomerId()" because "customer" is null
                switch (select) {
                    case "1":
                        createOrder(selectCustomer());
                        break;
                    case "2":
                        customer = selectCustomer();
                        orderService.showAllCustomersOrdersByID(customer);
                        orderService.deleteOrder(orderService.chooseOrderId(scanner, customer));
                        orderService.showAllCustomersOrdersByID(customer);
                        break;
                    case "3":
                        customer = selectCustomer();
                        //choose an order
                        Order order = orderService.getOrder(customer);
                        Product product = productService.extractProductFrom(order);
                        System.out.println("You can change the quantity for the product " + product.getProductName() +
                                ". There are " + product.getStockQuantity() +" units in stock\n");
                        orderService.changeOrder(product, order, customer);

                        break;
                case "4":
                    //good to add search by order history, e.g. by date, by product, by price, by category, by id
                    orderService.showAllCustomersOrdersByID(selectCustomer());
                    break;
                case "0":
                    System.out.println(BYE.getStyle());
                    return;
                default:
                    System.out.println(WRONG_OPTION.getStyle());
                    System.out.println("Please, provide the number 0-4 instead of " + "\"" + select + "\"\n");
            }
        } catch(Exception e){
            // Handle other errors (e.g. incorrect input)
            System.err.println("An unexpected error occurred: " + e.getMessage());
            scanner.nextLine();
        }
    }
        }



    /** private Customer selectCustomer() {

        try {
            System.out.println("Select a customer\n");
            return customerRepository.getCustomerByEmail(loginService.askEmail());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e)
        {
            System.out.println("Customer with this email " + email + " does not exist");
        }
        return null;
    }
*/
   private Customer selectCustomer() throws SQLException {
       String email = null;

           System.out.println("Select a customer");
           email = loginService.askEmail();
           Customer customer = customerRepository.getCustomerByEmail(email);
           if (customer == null) {
               System.err.println("\nCustomer with email " + email + " does not exist");
               return selectCustomer();
              } else {
                return customer;
           }

   }
   }

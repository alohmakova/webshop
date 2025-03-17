package order;

import admin.Admin;
import customer.Customer;
import customer.CustomerRepository;
import login.LoginService;
import product.Product;
import user.User;
import util.BaseLogger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;

import static util.TextStyle.*;

public class AdminOrderInteraction extends BaseLogger implements OrderController {

    LoginService loginService = new LoginService();
    CustomerRepository customerRepository = new CustomerRepository();
    Admin admin;
    Customer customer;

    @Override
    public void selectOrderOption(User user) {



        admin = (Admin) user;
        ArrayList<Order> orders;
        while (true) {
            try {

                System.out.println("\n=== Orders: back office ===\n" +
                        "1. Create order for the customer\n" +//it could be a fast order: when I do not show all products and categories - andmin only enter them manually or using search
                        "2. Delete order for the customer\n" +
                        "3. Change order for the customer\n" +
                        "4. Show order history for the customer\n" +
                        "0. Exit\n" +
                        OPTION.getStyle());
                logger.fine("The admin was prompted to select an option between 0 and 4 to manage orders or exit the program");
                String select = scanner.nextLine();

                switch (select) {
                    case "1":
                        logger.fine("Admin selected option 1: create an order for the customer");
                        createOrder(selectCustomer());
                        break;
                    case "2":
                        logger.fine("Admin selected option 2: delete an order for the customer");
                        customer = selectCustomer();
                        orders = orderService.showAllCustomersOrdersByID(customer);
                        if (orders.isEmpty()) {}
                        else {
                            orderService.deleteOrder(orderService.chooseOrderId(customer, orders), customer);
                            logger.fine("Admin can see that deleted order no longer exists in the database");
                        }
                        break;
                    case "3":
                        logger.fine("Admin selected option 3: change an order for the customer");
                        customer = selectCustomer();
                        logger.fine("After entering a valid existing in the database email, the system shows all customer's orders by customer's id");
                        orders = orderService.showAllCustomersOrdersByID(customer);
                        logger.fine("From all the customer's orders, the administrator selects the order to be changed by specifying its id");
                        Order order = orderService.chooseOrder(customer, orders);
                        logger.fine("When an order is selected the system receives information about the product that has been ordered");
                        Product product = productService.extractProductFrom(order);
                        logger.fine("The administrator receives information on how exactly to change the order");
                        logger.info("You can change the quantity for the product " + product.getProductName() +
                                ". There are " + product.getStockQuantity() +" units in stock\n");
                        logger.fine("The administrator changes the quantity of product in the order");
                        orderService.changeOrder(product, order, customer);
                        break;
                case "4":
                    //good to add search by order history, e.g. by date, by product, by price, by category, by id
                    logger.fine("Admin selected option 4: show order history for the customer");
                    orderService.showAllCustomersOrdersByID(selectCustomer());
                    break;
                case "0":
                    logger.fine("Admin selected option 0: exit the programme");
                    System.out.println(BYE.getStyle());
                    logger.fine("The programme must be restarted in order to return to work");
                    return;
                default:
                    logger.fine("Admin made a mistake when selecting the menu option");
                    System.err.println(WRONG_OPTION.getStyle());
                    System.err.println("Please, provide the number 0-4 instead of " + "\"" + select + "\"\n");
            }
        } catch(Exception e){
                logger.log(Level.SEVERE, e.getMessage(), e);
                System.err.println("An unexpected error occurred: press enter to continue working with orders or exit the programme ");
                scanner.nextLine();
        }
    }
        }

   private Customer selectCustomer() throws SQLException {

       String email = null;

       System.out.println("Select a customer");
       logger.fine("A customer's email is requested from the admin ");
           email = loginService.askEmail();
       logger.fine("The system retrieves customer data from database using email '" + email + "'");
           Customer customer = customerRepository.getCustomerByEmail(email);
           if (customer == null) {
               System.err.println("\nCustomer with email " + email + " does not exist");
               logger.warning("The email" + email + " does not exist in the database, a customer's email will be requested again ");
               return selectCustomer();
              } else {
               logger.fine("The user with the email '" + email + "' exists in the database. The customer object will be created");
                return customer;
           }

   }
   }

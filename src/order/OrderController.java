package order;

import admin.Admin;
import category.CategoryService;
import customer.Customer;
import login.LoginController;
import login.LoginService;
import product.Product;
import product.ProductService;
import util.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class OrderController {

    LoginController loginController;
    LoginService loginService;
    OrderService orderService;
    CategoryService categoryService;
    ProductService productService;
    Scanner scanner;

    // ANSI colours and formatting
    final String YELLOW = "\u001B[33m";
    final String RED = "\u001B[31m";
    final String BOLD = "\u001B[1m";
    final String RESET = "\u001B[0m";

    // Unicode symboler
    final String ATTENTION = "🚨";
    final String ARROW = "➜";

    //constructor for OrderController initialises services and scanner
    public OrderController() {
        this.loginController = new LoginController();
        this.loginService = new LoginService();
        this.orderService = new OrderService();
        this.categoryService = new CategoryService();
        this.productService = new ProductService();
        this.scanner = new Scanner(System.in);
    }

    /**Orderhantering
     ● Skapa nya ordrar
     ● Visa orderhistorik för kunder
     */
    public void run() {
        //At the moment only the customer can create an order but not the admin, this will need to be improved
        System.out.println(BOLD + RED + ARROW + " To create an order you need to log in as a customer! " + ATTENTION + "\n" + RESET);
        Customer customer = loginController.run();

        while (true) {
            try {

                System.out.println("\n=== Orders ===\n" +
                "1. Create new order\n" +
                "2. Show order history to the customer\n" +
                "0. Exit\n" +
                "Choose an option:\n ");

                int select = scanner.nextInt();

                switch (select) {
                    case 1:
                        //I use the categoryId to choose the product
                        categoryService.showAllCategoriesAsATable();//show all categories to the customer to choose
                        System.out.println("To create an order choose the product category (type the categoryId): \n");
                        int categoryId = scanner.nextInt();//categoryId validation required
                        //if there is 1 product in the category - ask amount, if there are 2 or more products - ask product id and amount
                        ArrayList<Product> products = productService.AllProductsByCaregoryIdAsArrayList(categoryId);
                        if (products.size() == 1) {
                            productService.showAllProductsByCaregoryIdAsATable(categoryId);//show all products from the category to choose
                            //to create the order I need product info
                            Product product = products.get(0);
                            //1-take the quantity from user's input, 2-create the order, calculating its price (totalAmount)
                            System.out.println("Enter the quantity: ");
                            int quantity = scanner.nextInt();//quantity validation required
                            //new order should be created with orderNumber, customerId, orderDate, productName, quantity, totalAmount
                            orderService.createNewOrder(customer, product, quantity);
                        }else if (products.size() > 1) {
                            productService.showAllProductsByCaregoryIdAsATable(categoryId);//show all products from the category to choose
                            System.out.println("Enter the product id: ");
                            int productId = scanner.nextInt();//productId validation required
                            //get the product by id
                            products.forEach(product -> {
                                product.getProductId();
                                if (product.getProductId() == productId) {
                                    System.out.println("Enter the quantity: ");
                                    int quantity = scanner.nextInt();//quantity validation required
                                    try {
                                        orderService.createNewOrder(customer, product, quantity);
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                }});

                        }else{
                            System.out.println("We are sorry, but there are no products in this category");
                        }
                        break;
                    case 2:
                        orderService.showAllCustomersOrdersByID(customer.getCustomerId());
                        break;
                    case 0:
                        System.out.println("Exit the program...");
                        return;
                    default:
                        System.out.println("Wrong option. Try again.");
                }
            } catch (Exception e) {
                // Handle other errors (e.g. incorrect input)
                System.out.println("An unexpected error occurred: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }
}

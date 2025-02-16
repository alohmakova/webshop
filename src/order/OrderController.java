package order;

import category.CategoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class OrderController {
    //create a menu

    OrderService orderService;
    //I use some methods from CategoryService class to get and show the info about product categories to create the order
    CategoryService categoryService;
    Scanner scanner;

    //constructor for OrderController initialises services and scanner
    public OrderController() {
        this.orderService = new OrderService();
        this.categoryService = new CategoryService();
        this.scanner = new Scanner(System.in);
    }

    /**Orderhantering
     ● Skapa nya ordrar
     ● Visa orderhistorik för kunder
     */
    public void run() {
        while (true) {
            try {

                System.out.println("\n=== Orderhantering ===\n" +
                "1. Skapa ny order\n" +
                "2. Visa orderhistorik för kunder\n" +
                "3. Avsluta\n" +
                "Välj ett alternativ: ");

                // Läs användarens val
                int select = scanner.nextInt();

                // Hantera användarens val
                switch (select) {
                    case 1:
                        //I use the categoryId to choose the product
                        categoryService.showAllCategoriesAsATable();//show all categories to the customer to choose
                        System.out.println("To create an order choose the product category (type the categoryId): \n");
                        int categoryId = scanner.nextInt();//categoryId validation required
                        //email is needed to get the id of the customer
                        System.out.println("Provide your email to log in");//validation and error handling, if that email is not in the database, if the format is wrong
                        String email = scanner.next();

                        //category+product+quantity is required to calculate the order price
                        //System.out.println("Choose product category");//validation and error handling
                        //String productCategory = scanner.next();
                        //System.out.println("Choose the product and its amount");//validation and error handling
                        //multiple products can be selected at once and at the same time that amounts can be selected
                        //order confirmation
                        //order created

                        //the order is created as an object and then this object still needs to be saved in the database
                        //order id and date are created automatically
                        orderService.createNewOrder(email);//email validation required
                        break;
                    case 2:
                        System.out.println("Enter customer's id: ");//it can be changed to enter customer's name/email
                        int customerId = scanner.nextInt();
                        orderService.showAllCustomersOrdersById(customerId);
                        break;
                    case 3:
                        System.out.println("Avslutar kundhantering...");
                        return;
                    default:
                        System.out.println("Ogiltigt val, försök igen");
                }
            } catch (Exception e) {
                // Handle other errors (e.g. incorrect input)
                System.out.println("Ett oväntat fel uppstod: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }
}

package order;

import category.CategoryService;
import product.Product;
import product.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class OrderController {
    //create a menu

    OrderService orderService;
    //I use some methods from CategoryService class to get and show the info about product categories to create the order
    CategoryService categoryService;
    //I use some methods from ProductService class to get and show the info about products to create the order
    ProductService productService;
    Scanner scanner;

    //constructor for OrderController initialises services and scanner
    public OrderController() {
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
                        //to create the order it is necessary to choose the product and it's amount
                        productService.showAllProductsByCaregoryIdAsATable(categoryId);//show all products from the category to choose
                        //if there is 1 product in the category - ask amount, if there are 2 or more products - ask id and amount
                        ArrayList<Product> products = productService.AllProductsByCaregoryIdAsArrayList(categoryId);
                        if (products.size() == 1) {
                            //to create the order I need product info
                            Product product = products.get(0);
                            //1-take the quantity from user's input, 2-log in or ask at least user's id, 3-create the order, calculating its price (totalAmount)
                            System.out.println("Enter the quantity: ");
                            int quantity = scanner.nextInt();//quantity validation required
                            //new order shoul be created with orderNumber, customerId, orderDate, productName, quantity, totalAmount
                            //I need email to get customer's id
                            System.out.println("Provide your email to log in");
                            String email = scanner.next();//email validation required
                            orderService.createNewOrder(email, product, quantity);


                        }else if (products.size() > 1) {
                            System.out.println("Enter the product id: ");
                        }else{
                            System.out.println("We are sorry, but there are no products in this category");
                        }
                        //multiple products can be selected at once and at the same time that amounts can be selected
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

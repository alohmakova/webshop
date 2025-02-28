package order;

import customer.Customer;
import product.Product;
import user.User;

import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerOrders implements OrderManager{


    @Override
    public void performActions(User user) {

        Customer customer = (Customer)user;

        while (true) {
            try {

                System.out.println("\n=== Orders ===\n" +
                        "1. Create new order\n" +
                        "2. Show order history to the customer\n" +
                        "0. Exit\n" +
                        PURPLE + ARROW + " Choose an option: "  + PLEASE + RESET + "\n");

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
                            System.out.println(PURPLE + ARROW + " Enter the quantity: "  + PLEASE + RESET + "\n");
                            int quantity = scanner.nextInt();//quantity validation required
                            //new order should be created with orderNumber, customerId, orderDate, productName, quantity, totalAmount
                            orderService.createNewOrder(customer, product, quantity);
                        } else if (products.size() > 1) {
                            productService.showAllProductsByCaregoryIdAsATable(categoryId);//show all products from the category to choose
                            System.out.println(PURPLE + ARROW + " Enter the product id "  + PLEASE + RESET + "\n");
                            int productId = scanner.nextInt();//productId validation required
                            //get the product by id
                            products.forEach(product -> {
                                product.getProductId();
                                if (product.getProductId() == productId) {
                                    System.out.println(PURPLE + ARROW + " Enter the quantity: "  + PLEASE + RESET + "\n");
                                    int quantity = scanner.nextInt();//quantity validation required
                                    try {
                                        orderService.createNewOrder(customer, product, quantity);
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });

                        } else {
                            System.out.println("We are sorry, but there are no products in this category");
                        }
                        break;
                    case 2:
                        orderService.showAllCustomersOrdersByID(customer.getCustomerId());
                        break;
                    case 0:
                        System.out.println("Exit the program...");
                        System.out.println(BOLD + YELLOW + ARROW + " Good bye!" + GOODBYE + "\n" + RESET);
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

package order;

import customer.Customer;
import product.Product;
import stock.StockService;
import user.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomerOrders implements OrderManager{

    /**Orderhantering
     ● Skapa nya ordrar
     ● Visa orderhistorik för kunder
     */

    @Override
    public void performActions(User user) {

        Customer customer = (Customer)user;

        while (true) {
            try {

                System.out.println("\n=== Orders ===\n" +
                        "1. Create new order\n" +
                        "2. Show order history\n" +
                        "0. Exit\n" +
                        PURPLE + ARROW + " Choose an option: "  + PLEASE + RESET + "\n");

                int select = scanner.nextInt();//validate that iser's input is int
                AtomicBoolean needProductQuantity = new AtomicBoolean(true);
                switch (select) {
                    case 1:
                        //I use the categoryId to choose the product
                        int categoryId = categoryService.chooseCategory(scanner);
                        //if there is 1 product in the category - ask amount, if there are 2 or more products - ask product id and amount
                        ArrayList<Product> products = productService.AllProductsByCaregoryIdAsArrayList(categoryId);
                        if (products.size() == 1) {
                            productService.showAllProductsByCaregoryIdAsATable(categoryId);//show all products from the category to choose
                            //to create the order I need product info
                            Product product = products.get(0);
                            //1-take the quantity from user's input, 2-create the order, calculating its price (totalAmount)

                            while (needProductQuantity.get()) {
                            System.out.println(PURPLE + ARROW + " Enter the quantity: "  + PLEASE + RESET + "\n");
                            int quantity = scanner.nextInt();//validate that iser's input is int
                                //boolean to run the loop if quantity provided by customer is not valid
                                needProductQuantity.set(orderService.validateQuantityInput(quantity, product, customer, scanner));
                            }
                        } else if (products.size() > 1) {

                            while (needProductQuantity.get()) {
                                productService.showAllProductsByCaregoryIdAsATable(categoryId);//show all products from the category to choose
                                System.out.println(PURPLE + ARROW + " Enter the product id " + PLEASE + RESET + "\n");
                                int productId = scanner.nextInt();//validate that iser's input is int
                                //get the product by id
                                products.forEach(product -> {
                                    product.getProductId();
                                    if (product.getProductId() == productId) {
                                        System.out.println(PURPLE + ARROW + " Enter the quantity: " + PLEASE + RESET + "\n");
                                        int quantity = scanner.nextInt();//quantity validation required
                                        try {
                                            needProductQuantity.set(orderService.validateQuantityInput(quantity, product, customer, scanner));
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                });
                            }
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

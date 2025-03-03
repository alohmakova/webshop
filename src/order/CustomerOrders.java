package order;

import customer.Customer;
import product.Product;
import user.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomerOrders implements OrderManager{

    /**Orderhantering
     ✔️Skapa nya ordrar
     ️ ✔️Visa orderhistorik för kunder
     */

    boolean zeroInput = true;
    Customer customer;

    @Override
    public void performActions(User user) {

        customer = (Customer)user;

        while (true) {
            try {

                System.out.println("\n=== Orders ===\n" +
                        "1. Create new order\n" +
                        "2. Show order history\n" +
                        "0. Exit\n" +
                        PURPLE + ARROW + " Choose an option: "  + PLEASE + RESET + "\n");

                int select = scanner.nextInt();//validate that iser's input is int
                //AtomicBoolean zeroInput = new AtomicBoolean(true);
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
                            zeroInput = true;
                            while (zeroInput) {
                                System.out.println(PURPLE + ARROW + " Enter the quantity: " + PLEASE + RESET + "\n");
                                int quantity = scanner.nextInt();//validate that iser's input is int
                                //boolean to run the loop if quantity provided by customer is 0 or less
                                handleOrder(quantity, product);
                            }
                        } else if (products.size() > 1) {
                            zeroInput = true;
                            while (zeroInput) {
                                productService.showAllProductsByCaregoryIdAsATable(categoryId);//show all products from the category to choose
                                System.out.println(PURPLE + ARROW + " Enter the product id " + PLEASE + RESET + "\n");
                                int productId = scanner.nextInt();//validate that iser's input is int

                                /**products.forEach(product -> {
                                    product.getProductId();
                                    if (product.getProductId() == productId) {
                                        System.out.println(PURPLE + ARROW + " Enter the quantity: " + PLEASE + RESET + "\n");
                                        int quantity = scanner.nextInt();//quantity validation required
                                        try {
                                            handleOrder(quantity, product);
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                });*/
                                //get the product by id
                                for (Product product : products) {
                                    if (product.getProductId() == productId) {
                                        System.out.println(PURPLE + ARROW + " Enter the quantity: " + PLEASE + RESET + "\n");
                                        int quantity = scanner.nextInt(); // quantity validation required
                                        try {
                                            handleOrder(quantity, product);
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
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

     /**Lagerhantering
      ✔️Uppdatera lagersaldo för produkter (stock_quantity ska uppdateras efter order lagts)
     ● Kontrollera lagerstatus (man ska inte kunna lägga till en produkt i en order om den inte finns på lager)
     */
    private void handleOrder(int quantity, Product product) throws SQLException {
        zeroInput = orderService.validateQuantityInput(quantity, product);
        if (!zeroInput && quantity <= product.getStockQuantity())
        {
            //create order
            orderService.createNewOrder(customer, product, quantity);
            //stock_quantity is updated after order placed
            productService.reduceStockQuantity(product, quantity);
        }
    }


}

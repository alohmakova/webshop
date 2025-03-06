package order;

import customer.Customer;
import product.Product;
import user.User;
import java.util.ArrayList;

public class CustomerOrderController implements OrderManager{

    /**Orderhantering
     ✔️Skapa nya ordrar
     ️ ✔️Visa orderhistorik för kunder
     */

    /**Lagerhantering
     ✔️Uppdatera lagersaldo för produkter (stock_quantity ska uppdateras efter order lagts)
     ● Kontrollera lagerstatus (man ska inte kunna lägga till en produkt i en order om den inte finns på lager):
     In this case I have a choice -
     - not to show a product with 0 or less stock_quantity or
     - to show it, but not to let the customer order it.
     I find the first option easier and more logical.
     I can show a product with 0 or less stock_quantity to an administrator, but not to a regular customer.
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

                String select = scanner.nextLine();

                switch (select) {
                    case "1":

                        /**
                         * To create an order I need product info
                         * I use the categoryId to choose the product
                         * categoryId validation is done inside the method chooseCategory(scanner)
                         * */

                        categoryService.showAllCategoriesAsATable();
                        int categoryId = categoryService.chooseCategory(scanner);
                        ArrayList<Product> products = productService.AllProductsByCaregoryIdAsArrayList(categoryId);

                        /**
                         * ➡ if there is 1 product in the category - ask amount,
                         * ➡ if there are 2 or more products - ask product id and amount
                         * */

                        if (products.size() == 1) {
                            productService.showAllProductsByCaregoryIdAsATable(categoryId);
                            Product product = products.getFirst();//the product is automatically chosen because it is the only one
                            orderService.processOrder(product, customer);
                        } else if (products.size() > 1) {
                            productService.showAllProductsByCaregoryIdAsATable(categoryId);
                            int productId = productService.chooseProduct(scanner);
                                for (Product product : products) {
                                    if (product.getProductId() == productId) {//if the product id from user's input matches the id from the list from database
                                        orderService.processOrder(product, customer);
                                    }
                                }

                        } else {
                            System.out.println("We are sorry, but there are no products in this category");
                        }
                        break;
                    case "2":
                        orderService.showAllCustomersOrdersByID(customer.getCustomerId());
                        break;
                    case "0":
                        System.out.println("Exit the program...");
                        System.out.println(BOLD + YELLOW + ARROW + " Good bye!" + GOODBYE + "\n" + RESET);
                        return;
                    default:
                        System.out.println("Wrong option. Try again.");
                        System.out.println("Please, provide the number 0, 1 or 2 instead of " + "\"" + select + "\"\n");
                }
            } catch (Exception e) {
                // Handle other errors (e.g. incorrect input)
                System.out.println("An unexpected error occurred: " + e.getMessage());
                scanner.nextLine();
            }
        }

        }




}

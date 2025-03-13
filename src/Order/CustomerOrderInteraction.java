package order;

import customer.Customer;
import user.User;
import util.BaseLogger;

import java.util.logging.Level;

import static util.TextStyle.*;

public class CustomerOrderInteraction extends BaseLogger implements OrderController {

    /**Orderhantering
     ✔️Skapa nya ordrar
     ️ ✔️Visa orderhistorik för kunder
     */

    /**Lagerhantering
     ✔️Uppdatera lagersaldo för produkter (stock_quantity ska uppdateras efter order lagts)
     ✔️Kontrollera lagerstatus (man ska inte kunna lägga till en produkt i en order om den inte finns på lager):
     In this case I have a choice -
     - not to show a product with 0 or less stock_quantity or
     - to show it, but not to let the customer order it.
     I find the first option easier and more logical.
     I can show a product with 0 or less stock_quantity to an administrator, but not to a regular customer.
     */

    Customer customer;


    @Override
    public void selectOrderOption(User user) {


        customer = (Customer)user;
        logger.info("The user is logged in as a customer");

        while (true) {
            try {

                System.out.println("\n=== Orders ===\n" +
                        "1. Create new order\n" +
                        "2. Show order history\n" +
                        "0. Exit\n" +
                        OPTION.getStyle());

                String select = scanner.nextLine();

                switch (select) {
                    case "1":
                        logger.fine("Customer selected option 1: create new order ");
                        createOrder(customer);
                        break;
                    case "2":
                        logger.fine("Customer selected option 2: show order history ");
                        orderService.showLimitedCustomersOrdersByID(customer.getCustomerId());
                        break;
                    case "0":
                        logger.fine("Customer selected option 0: exit the programme ");
                        System.out.println(BYE.getStyle());
                        logger.fine("The programme must be restarted in order to return to work ");
                        return;
                    default:
                        logger.fine("Customer made a mistake when selecting the menu option ");
                        System.out.println(WRONG_OPTION.getStyle());
                        System.out.println("Please, provide the number 0, 1 or 2 instead of " + "\"" + select + "\"\n");
                }
            } catch (Exception e) {
                // Handle other errors
                logger.log(Level.SEVERE, e.getMessage(), e);
                System.err.println("An unexpected error occurred: press enter to continue working with orders or exit the programme ");
                scanner.nextLine();
            }
        }

        }


}

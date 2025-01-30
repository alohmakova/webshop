package order;

import java.util.Scanner;

public class OrderController {
    //create a menu

    OrderService orderService;
    Scanner scanner;

    //constructor for OrderController initialises service and scanner

    public OrderController() {
        this.orderService = new OrderService();
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
                        orderService.createNewOrder();// not implemented
                        break;
                    case 2:
                        System.out.println("Enter customer's id: ");//it can be changed to enter customer's name. Need to write method to get id by name
                        int customerId = scanner.nextInt();
                        orderService.showAllCustomersOrdersById(customerId);// not implemented
                        break;
                    case 3:
                        System.out.println("Avslutar kundhantering...");
                        return;
                    default:
                        System.out.println("Ogiltigt val, försök igen");
                }
            } catch (Exception e) {
                // Hantera övriga fel (t.ex. felaktig input)
                System.out.println("Ett oväntat fel uppstod: " + e.getMessage());
                scanner.nextLine(); // Rensa scanner-bufferten vid felinmatning
            }
        }
    }
}

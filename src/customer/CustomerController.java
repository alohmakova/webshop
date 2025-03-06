package customer;
import java.sql.SQLException;
import java.util.Scanner;
import static util.TextStyle.*;

/**
 * Applikationen ska struktureras i tre huvudsakliga lager:
 * Der är Controller-lager med konsolbaserat användargränssnitt
 * Nämligen Controller-klass för kundhantering
 * Hanterar användarinteraktion för kundrelaterade operationer (there is a menu displayed to the user)
 */
public class CustomerController {

    // Service-lager för kundhantering, hanterar affärslogik
    CustomerService customerService;

    // Scanner för användarinput
    Scanner scanner;

    /**
     * Konstruktor för CustomerController
     * Initierar service och scanner
     */
    public CustomerController() {
        // Skapa instanser av nödvändiga objekt
        this.customerService = new CustomerService();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Huvudloop för kundhantering
     * Visar meny och hanterar användarval
     */
    public void run() {
        while (true) {
            try {
                // Skriv ut menyalternativ direkt i run-metoden för tydlighet
                System.out.println("\n=== Customer management ===");
                System.out.println("1. Show all customers");
                System.out.println("0. Exit");
                System.out.print(OPTION.getStyle());

                /**Man behöver ändra Kundhantering enligt kraven i uppgiften:
                 ● Registrera nya kunder
                 ● Uppdatera befintlig kundinformation
                 */

                // Läs användarens val
                String select = scanner.nextLine();

                // Hantera användarens val
                switch (select) {
                    case "1":
                        // Anropa service-lagret för att visa alla kunder
                        customerService.showAllUsers();
                        break;
                    case "0":
                        System.out.println("Avslutar kundhantering...");
                        return;
                    default:
                        System.out.println(WRONG_OPTION.getStyle());
                }
            } catch (SQLException e) {
                // Hantera databasfel
                System.out.println("Ett fel uppstod vid databasanrop: " + e.getMessage());
            } catch (Exception e) {
                // Hantera övriga fel (t.ex. felaktig input)
                System.out.println("Ett oväntat fel uppstod: " + e.getMessage());
                scanner.nextLine(); // Rensa scanner-bufferten vid felinmatning
            }
        }
    }
}


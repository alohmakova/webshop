package order;

import admin.Admin;
import customer.Customer;
import login.LoginController;
import login.LoginService;
import user.User;


public class OrderLoginController {

    LoginController loginController;
    LoginService loginService;
    OrderManager orderManager;

    User user;

    // ANSI colours and formatting
    //To use the 256 color codes, your sequence will look like:
    // "\u001b[38;5;<color code>m + output text" for text color
    // "\u001b[48;5;<color code>m + output text" for background color
    final String YELLOW = "\u001B[38;5;227m";//
    final String RED = "\u001B[31m";
    final String BLUE = "\u001B[34m";
    final String BOLD = "\u001B[1m";
    final String RESET = "\u001B[0m";



    // Unicode symboler
    final String ATTENTION = "🚨";
    final String ARROW = "➜";
    final String GOODBYE = "👋";
    final String HEART = "💙";


    public OrderLoginController() {
        this.loginController = new LoginController();
        this.loginService = new LoginService();
        this.orderManager = new CustomerOrderController();

    }

    //To work with orders it is necessary to log in first
    //You need to log in as a customer to create an order
    //Right now it is not possible to work with orders like administrator
    //You can also choose not to log in and exit the program
    public void run() {
        //At the moment only the customer can create an order but not the admin, this will need to be improved
        System.out.println(BOLD + BLUE + ARROW + " To create an order you need to log in as a customer! " + HEART + "\n" + RESET);
        user = loginController.run();
        if (user == null) {
            System.out.println(BOLD + YELLOW + ARROW + " Good bye!" + GOODBYE + "\n" + RESET);
        }else if (user instanceof Customer customer) {
            orderManager.performActions(customer);
        }else if (user instanceof Admin) {
            boolean userIsAdmin = true;
                while(userIsAdmin) {
                System.out.println(BOLD + RED + ATTENTION + " Admin can't create an order. To create an order you need to log in as a customer! " + ATTENTION + "\n" + RESET);
                user = loginController.run();
                    if (user == null) {
                        userIsAdmin = false;
                        System.out.println(BOLD + YELLOW + ARROW + " Good bye!" + GOODBYE + "\n" + RESET);
                    }else if (user instanceof Customer customer) {
                    userIsAdmin = false;
                        orderManager.performActions(customer);
                }
            }

        }
    }
}

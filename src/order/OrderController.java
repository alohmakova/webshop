package order;

import admin.Admin;
import customer.Customer;
import login.LoginController;
import login.LoginService;
import user.User;
import static util.TextStyle.*;

/**Orderhantering
 ✔️ Skapa nya ordrar
 ✔️ Visa orderhistorik för kunder
 */

public class OrderController {

    LoginController loginController;
    LoginService loginService;
    OrderManager orderManager;

    User user;

    public OrderController() {
        this.loginController = new LoginController();
        this.loginService = new LoginService();
        this.orderManager = new CustomerOrders();

    }

    public void run() {
        //At the moment only the customer can create an order but not the admin, this will need to be improved
        System.out.println (BOLD.getStyle() +
                            BLUE.getStyle() +
                            ARROW.getStyle() + " To create an order you need to log in as a customer! " +
                            HEART.getStyle() + "\n" +
                            RESET.getStyle());
        user = loginController.run();
        if (user == null) {
            //If I use this line, it is printed twice, because when I exit Orders, it is also printed
            //System.out.println(TextStyle.BYE.getStyle());
        }else if (user instanceof Customer customer) {
            orderManager.performActions(customer);
        }else if (user instanceof Admin) {
            boolean userIsAdmin = true;
                while(userIsAdmin) {
                System.out.println (BOLD.getStyle() +
                                    RED.getStyle() +
                                    ATTENTION.getStyle() + " Admin can't create an order. To create an order you need to log in as a customer! " +
                                    ATTENTION.getStyle() + "\n" +
                                    RESET.getStyle());
                user = loginController.run();
                    if (user == null) {
                        userIsAdmin = false;
                        //If I use this line, it is printed twice, because when I exit Orders, it is also printed
                        //System.out.println(TextStyle.BYE.getStyle());
                    }else if (user instanceof Customer customer) {
                    userIsAdmin = false;
                        orderManager.performActions(customer);
                }
            }

        }
    }
}

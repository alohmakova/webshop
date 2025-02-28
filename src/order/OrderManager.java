package order;

import category.CategoryService;
import product.ProductService;
import user.User;

import java.util.Scanner;

public interface OrderManager {
    User user = null;
    Scanner scanner = new Scanner(System.in);
    OrderService orderService = new OrderService();
    CategoryService categoryService = new CategoryService();
    ProductService productService = new ProductService();

    String YELLOW = "\u001B[38;5;227m";//
    String PURPLE = "\u001b[38;5;171m";
    String BOLD = "\u001B[1m";
    String RESET = "\u001B[0m";


    String ARROW = "➜";
    String GOODBYE = "👋";
    String PLEASE = "🙏";


    void performActions(User user);
}

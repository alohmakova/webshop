package order;

import category.CategoryService;
import product.ProductService;
import user.User;
import util.TextStyle;

import java.util.Scanner;

public interface OrderManager {

    Scanner scanner = new Scanner(System.in);
    OrderService orderService = new OrderService();
    CategoryService categoryService = new CategoryService();
    ProductService productService = new ProductService();


    TextStyle PURPLE = TextStyle.PURPLE;
    TextStyle RESET = TextStyle.RESET;


    TextStyle ARROW = TextStyle.ARROW;
    TextStyle PLEASE = TextStyle.PLEASE;


    void performActions(User user);
}

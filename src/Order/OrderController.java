package order;

import category.CategoryService;
import customer.Customer;
import product.*;
import user.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public interface OrderController {


    Scanner scanner = new Scanner(System.in);
    OrderService orderService = new OrderService();
    CategoryService categoryService = new CategoryService();
    ProductService productService = new ProductService();


    void selectOrderOption(User user) throws SQLException;

    default void createOrder(Customer customer) throws SQLException {
        //choose a category
        int categoryId = categoryService.getCategoryId();

        //choose a product from the category
        ArrayList<Product> products = productService.AllProductsByCaregoryIdAsArrayList(categoryId);

        if (products.isEmpty()) {
            System.err.println("We are sorry, but there are no products in category " + categoryId + ". Select another category.");
            createOrder(customer);
            return;
        }

        //ternary operator
        SelectProduct selectProduct = (products.size() == 1) ? new OneProductInCategory() : new ManyProductsInCategory();
        Product selectedProduct = selectProduct.selectProduct(categoryId, products);

        //create an order
        int quantity = orderService.chooseQuantityFor(selectedProduct);
        orderService.newOrder(customer, selectedProduct, quantity);
        productService.reduceStockQuantity(selectedProduct, quantity);
    }


}

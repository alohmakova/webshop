package order;

import category.CategoryService;
import customer.Customer;
import product.*;
import user.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public interface OrderManager {

    Scanner scanner = new Scanner(System.in);
    OrderService orderService = new OrderService();
    CategoryService categoryService = new CategoryService();
    ProductService productService = new ProductService();



    void selectOrderOption(User user) throws SQLException;

    default void createOrder(Customer customer) throws SQLException {
        //choose a category
        int categoryId = categoryService.getCategoryIdFromCustomersInput();
        //choose a product from the category
        ArrayList<Product> products = productService.AllProductsByCaregoryIdAsArrayList(categoryId);
        SelectProduct selectProduct = null;
        if (products.size() == 1) {
            selectProduct = new OneProductInCategory();
        } else if (products.size() > 1) {
            selectProduct = new ManyProductsInCategory();
        } else {
            System.err.println("We are sorry, but there are no products in this category. An order cannot be created.");
        }
        Product selectedProduct = selectProduct.selectProduct(categoryId, products);
        //create an order
        orderService.newOrder(customer, selectedProduct, orderService.chooseQuantity(selectedProduct));
        productService.reduceStockQuantity(selectedProduct, orderService.chooseQuantity(selectedProduct));
    }
}

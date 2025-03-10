package product;

import java.sql.SQLException;
import java.util.ArrayList;

import static order.OrderController.scanner;

public class ManyProductsInCategory implements SelectProduct {

    @Override
    public Product selectProduct(int categoryId, ArrayList<Product> products) throws SQLException {

            productService.showAllProductsByCategoryIdAsATable(categoryId);
            int productId = productService.chooseProduct(scanner);
            for (Product product : products) {
                if (product.getProductId() == productId) {//if the product id from user's input matches the id from the list from a database
                    return product;
                }
            }
            return null;
}
}
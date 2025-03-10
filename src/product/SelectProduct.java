package product;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SelectProduct {

    ProductService productService = new ProductService();
    Product selectProduct(int categoryId, ArrayList<Product> products) throws SQLException;
}

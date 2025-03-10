package product;

import java.sql.SQLException;
import java.util.ArrayList;

public class OneProductInCategory implements SelectProduct{

    @Override
    public Product selectProduct(int categoryId, ArrayList<Product> products) throws SQLException {
            productService.showAllProductsByCategoryIdAsATable(categoryId);
            return products.getFirst();//the product is automatically chosen because it is the only one
    }
}

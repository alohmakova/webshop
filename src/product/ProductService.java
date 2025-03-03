package product;

import customer.Customer;

import java.sql.SQLException;
import java.util.ArrayList;

public class ProductService {

    //fields
    ProductRepository productRepository;

    //constructor initialises the repository layer
    public ProductService() {this.productRepository = new ProductRepository();}

    //Check if we have any products to show - not implemented
    public void showAllProductsByCaregoryIdAsATable(int id) throws SQLException {
        ArrayList<Product> products = productRepository.getProductsbyCategoryId(id);
        StringBuilder sb = new StringBuilder();
        sb.append("+------+------------------+----------+----------------+\n");
        sb.append("|  ID  |   Product name   |  Price   | Stock quantity |\n");
        sb.append("+------+------------------+----------+----------------+\n");
        //I'm replacing the traditional for-each loop with a lambda
        /*for (Product product : products) {
            sb.append(String.format("| %-4d | %-16s | %-8.2f | %-14d |\n",
                    product.getProductId(),
                    product.getProductName(),
                    product.getProductPrice(),
                    product.getStockQuantity()));
        }*/
        products.forEach(product -> sb.append(String.format("| %-4d | %-16s | %-8.2f | %-14d |\n",
                product.getProductId(),
                product.getProductName(),
                product.getProductPrice(),
                product.getStockQuantity())));
        sb.append("+------+------------------+----------+----------------+\n");
        sb.toString();
        System.out.println(sb);
    }

    public ArrayList<Product> AllProductsByCaregoryIdAsArrayList(int id) throws SQLException {
        return productRepository.getProductsbyCategoryId(id);
    }

    public void reduceStockQuantity(Product product, int quantity) throws SQLException {
        int newQuantity = product.getStockQuantity() - quantity;
        productRepository.updatePruductsQuantity(product.getProductName(), newQuantity);
    }
}

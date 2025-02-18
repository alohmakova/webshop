package product;

import java.sql.SQLException;
import java.util.ArrayList;

public class ProductService {

    //fields
    ProductRepository productRepository;

    //constructor initialises the repository layer
    public ProductService() {this.productRepository = new ProductRepository();}

    public void showAllProductsByCaregoryIdAsATable(int id) throws SQLException {
        ArrayList<Product> products = productRepository.getProductsbyCategoryId(id);
        StringBuilder sb = new StringBuilder();
        sb.append("+------+------------------+----------+----------------+\n");
        sb.append("|  ID  |   Product name   |  Price   | Stock quantity |\n");
        sb.append("+------+------------------+----------+----------------+\n");

        for (Product product : products) {
            sb.append(String.format("| %-4d | %-16s | %-8.2f | %-14d |\n",
                    product.getProductId(), product.getProductName(), product.getProductPrice(), product.getStockQuantity()));
        }
        sb.append("+------+------------------+----------+----------------+\n");
        sb.toString();
        System.out.println(sb);
    }

    public void chooseProductAndAmount(int id) throws SQLException {
        ArrayList<Product> products = productRepository.getProductsbyCategoryId(id);
        if (products.size() == 1) {
            System.out.println("Enter the quantity: ");
        }else if (products.size() > 1) {
            System.out.println("Enter the product id: ");
        }else{
            System.out.println("We are sorry, but there are no products in this category");
        }
    }

    public ArrayList<Product> AllProductsByCaregoryIdAsArrayList(int id) throws SQLException {
        return productRepository.getProductsbyCategoryId(id);
    }
}

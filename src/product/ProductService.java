package product;

import util.InvalidIDException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ProductService {

    //fields
    ProductRepository productRepository;

    //constructor initialises the repository layer
    public ProductService() {this.productRepository = new ProductRepository();}

    //Check if we have any products to show - not implemented
    public void showAllProductsByCategoryIdAsATable(int id) throws SQLException {
        ArrayList<Product> products = productRepository.getStockProductsByCategoryId(id);
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
        return productRepository.getStockProductsByCategoryId(id);
    }

    public void reduceStockQuantity(Product product, int quantity) {
        int newQuantity = product.getStockQuantity() - quantity;
        productRepository.updateProductsQuantity(product.getProductName(), newQuantity);
    }

    public int chooseProduct(Scanner scanner) throws SQLException {
        System.out.println("Enter the product id:");
        //I get a list of all existing product id's
        ArrayList<Product> products = productRepository.getAllProductsFromStock();
        ArrayList<Integer> productsID = new ArrayList<>();
        products.forEach(product -> productsID.add(product.getProductId()));

        int productID;

        try {
            productID = Integer.parseInt(scanner.nextLine());//get the category id from the user
            if(!productsID.contains(productID)){//in case the user enters an id that is not in the list
                throw new InvalidIDException("Ooops... provided id doesn't exist in the list🤷‍♂️!");//I created my own exception
            }
        }catch (NumberFormatException e){//in case the user does not enter a number
            System.out.println("Ooops... provided input doesn't look like id🤔!");

            return chooseProduct(scanner);
        } catch (InvalidIDException e) {
            System.out.println(e.getMessage());
            return chooseProduct(scanner);
        }
        return productID;
    }
}

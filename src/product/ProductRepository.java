package product;

import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ProductRepository {

    public ArrayList<Product> getProductsbyCategoryId(int id) throws SQLException {
        ArrayList<Product> products = new ArrayList<>();

        //I use PreparedStatement because I send id as a parameter
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT * FROM products as p join products_categories as pc on p.product_id = pc.product_id where category_id = ?")) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery();) {

                while (rs.next()) {
                    Product product = new Product(
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("stock_quantity"));
                    products.add(product);
                }

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    public ArrayList<Product> getAllProductsFromStock() throws SQLException {
        ArrayList<Product> products = new ArrayList<>();

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT * FROM products WHERE stock_quantity > 0")) {

            try (ResultSet rs = pstmt.executeQuery();) {

                while (rs.next()) {
                    Product product = new Product(
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("stock_quantity"));
                    products.add(product);
                }

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    public ArrayList<Product> getProductsOutOfStock() throws SQLException {
        ArrayList<Product> products = new ArrayList<>();

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT * FROM products WHERE stock_quantity = 0")) {

            try (ResultSet rs = pstmt.executeQuery();) {

                while (rs.next()) {
                    Product product = new Product(
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("stock_quantity"));
                    products.add(product);
                }

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    ////Update stock balance for products (stock_quantity should be updated after order placed)
    //If a product is out of stock after creating an order, I don't remove it from the database, but do I want to display it to the customer or only to admin?
    public void updatePruductsQuantity(String name, int updatedQuantity) throws SQLException {

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE products SET stock_quantity = ? WHERE name = ?")) {

            pstmt.setInt(1, updatedQuantity);
            pstmt.setString(2, name);
            pstmt.executeUpdate();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package product;

import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductRepository {

    public ArrayList<Product> getStockProductsByCategoryId(int id) throws SQLException {
        ArrayList<Product> products = new ArrayList<>();

        //I use PreparedStatement because I send id as a parameter
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT * FROM products as p join products_categories as pc on p.product_id = pc.product_id where category_id = ? AND stock_quantity > 0")) {

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
        }
        return products;
    }

    public ArrayList<Product> getAllProductsFromStock() {
        ArrayList<Product> products = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public ArrayList<Product> getProductsOutOfStock() {
        ArrayList<Product> products = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT * FROM products WHERE stock_quantity =< 0")) {

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    /// /Update stock balance for products (stock_quantity should be updated after order placed)
    //If a product is out of stock after creating an order, I don't remove it from the database, but do I want to display it to the customer or only to admin?
    public void updateProductsQuantity(String name, int updatedQuantity) {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE products SET stock_quantity = ? WHERE name = ?")) {

            pstmt.setInt(1, updatedQuantity);
            pstmt.setString(2, name);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*public Product getProductByName(String name) {
        Product product = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "select * from products where name = ?")) {

            pstmt.setString(1, name);

            try (ResultSet rs = pstmt.executeQuery();) {

                            product = new Product(
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("stock_quantity"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Product with name " + name + " not found");
            e.printStackTrace();
        }
            return product;
    }*/
    public Product getProductByName(String name) {
        Product product = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT * FROM products WHERE name = ?")) {

            pstmt.setString(1, name);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    product = new Product(
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("stock_quantity"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Product with name " + name + " not found");
            e.printStackTrace();
        }

        return product;
    }

}
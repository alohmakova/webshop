package product;

import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
}

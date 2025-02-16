package category;

import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CategoryRepository {
    public ArrayList<Category> getAllCategories() throws SQLException {
        ArrayList<Category> categories = new ArrayList<>();
        try(Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement()){

            String sql = "SELECT * FROM categories";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                Category category = new Category(
                        rs.getInt("category_id"),
                        rs.getString("name"));
                categories.add(category);
            }

        }
        return categories;
    }
}

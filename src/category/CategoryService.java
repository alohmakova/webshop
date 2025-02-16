package category;

import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryService {
    //fields
    CategoryRepository categoryRepository;

    //constructor initialises the repository layer
    public CategoryService() {
        categoryRepository = new CategoryRepository();
    }

    public void showAllCategoriesAsATable() throws SQLException {
        ArrayList<Category> categories = categoryRepository.getAllCategories();
        StringBuilder sb = new StringBuilder();
        sb.append("+------------+--------------+\n");
        sb.append("| categoryId | categoryName |\n");
        sb.append("+------------+--------------+\n");

        for (Category category : categories) {
            sb.append(String.format("| %-10d | %-12s |\n", category.getCategoryId(), category.getCategoryName()));
        }

        sb.append("+------------+--------------+");
        sb.toString();
        System.out.println(sb);
    }

    public void showAllCategories() throws SQLException {
        System.out.println(categoryRepository.getAllCategories());
    }
}

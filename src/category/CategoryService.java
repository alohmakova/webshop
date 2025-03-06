package category;
import util.InvalidIDException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class CategoryService {
    //fields
    CategoryRepository categoryRepository;

    //constructor initialises the repository layer
    public CategoryService() {
        categoryRepository = new CategoryRepository();
    }

    //Check if we have any сategories to show - not implemented
    public void showAllCategoriesAsATable() throws SQLException {
        ArrayList<Category> categories = categoryRepository.getAllCategories();
        StringBuilder sb = new StringBuilder();
        sb.append("+------------+--------------+\n");
        sb.append("| categoryId | categoryName |\n");
        sb.append("+------------+--------------+\n");
        //I'm replacing the traditional for-each loop with a lambda
        /*for (Category category : categories) {
            sb.append(String.format("| %-10d | %-12s |\n",
                    category.getCategoryId(),
                    category.getCategoryName()));
        }*/
        categories.forEach(category -> sb.append(String.format("| %-10d | %-12s |\n",
                category.getCategoryId(),
                category.getCategoryName())));

        sb.append("+------------+--------------+");
        sb.toString();
        System.out.println(sb);
    }

    public void showAllCategories() throws SQLException {
        System.out.println(categoryRepository.getAllCategories());
    }

    //categoryId validation is done here
    public int chooseCategory(Scanner scanner) throws SQLException {

        System.out.println("To create an order choose the product category ➡️ type the categoryId from the list provided above👆⬆️☝️: \n");//ask to choose the category

        //I get a list of all existing category id's
        ArrayList<Category> categories = categoryRepository.getAllCategories();
        ArrayList<Integer> categoriesID = new ArrayList<>();
        categories.forEach(category -> categoriesID.add(category.getCategoryId()));

        int categoryID;

        try {
            categoryID = Integer.parseInt(scanner.nextLine());//get the category id from the user
            if(!categoriesID.contains(categoryID)){//in case the user enters an id that is not in the list
                throw new InvalidIDException("Ooops... provided id doesn't exist in the list🤷‍♂️!");//I created my own exception
            }
        }catch (NumberFormatException e){//in case the user does not enter a number
            System.out.println("Ooops... provided input doesn't look like id🤔!");

            /**
             * call return chooseCategory(scanner); is recursion.
             * This means that the method calls itself
             * ️️ ️1️⃣if the entered data is not a number ⃣or
             * 2️⃣if the entered categoryID does not exist in the list of categories.
             * Recursion is used to request input from the user again until a valid categoryID is entered.
             * */

            return chooseCategory(scanner);
        } catch (InvalidIDException e) {
            System.out.println(e.getMessage());
            return chooseCategory(scanner);
        }
        return categoryID;
    }
}

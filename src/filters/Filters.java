package filters;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Filters<T> {

    /**A common template for creating a method in a generic class
    public T filter(T t) {
        return t;
    }*/
////--------------------------------------------------------------------------------------------------------------------
 /**
 *      * Filters the collection by search string.
 *      * @param list - list of objects
 *      * @param searchString - search string
 *      * @param nameExtractor function - how to get the name from the object
 *      * @return filtered list of objects containing the search string
 *
 */
    public List<T> filterByName(List<T> list, String searchString, Function<T, String> nameExtractor) {
        //checking for null protects the code from java.lang.NullPointerException: Cannot invoke "List.stream()" because "list" is null
        if (list == null || searchString == null || nameExtractor == null) {
            return new ArrayList<>();
        }

        return list.stream()//сreate a stream of items from the list
                //nameExtractor.apply(item) - calls a function to get a name from an object. For an Order object, the function returns order.getProductName().
                .filter(item -> nameExtractor.apply(item).toLowerCase().contains(searchString.toLowerCase()))
                .collect(Collectors.toList());//collect the filtered items into a new list.
    }
}

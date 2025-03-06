package order;

import customer.Customer;
import product.Product;
import product.ProductService;
import util.InvalidQuantityException;

import java.sql.SQLException;
import java.util.ArrayList;

import static order.OrderManager.scanner;

public class OrderService {

    // fields
    OrderRepository orderRepository;
    ProductService productService = new ProductService();

    //constructor initialises the repository layer
    public OrderService() {
        this.orderRepository = new OrderRepository();
    }

    //Check if we have any orders to show - not implemented
    public void showAllCustomersOrdersById(int customerId) throws SQLException {//need to throw SQLException otherwise it will not work
        System.out.println(orderRepository.getAllOrdersByCustomerId(customerId));

    }

    //new order should be created with orderNumber, customerId, orderDate, productName, quantity, totalAmount
    public void createNewOrder(Customer customer, Product product, int quantity) throws SQLException {
try {
        if(product.getStockQuantity() >= quantity){
            Order order = new Order(
                    orderRepository.getMaxOrderId()+1,//int orderNumber
                    customer.getCustomerId(),                       //int customerId
                    product.getProductName(),                       //String productName
                    quantity,                                       //int quantity
                    quantity*product.getProductPrice());            //double totalAmount

            //save new order in the database in orders table
            orderRepository.addOrder(order);
            //save new order in the database in orders_products table
            orderRepository.insertIntoOrdersProducts(order, product);


            System.out.println("Thank you, order with the number " + order.orderNumber +" has been successfully created!\nOrder details: ");
            //show order details to the customer
            showOrderDetailsAsATable(order);

        } else {

            throw new InvalidQuantityException("There are only " + product.getStockQuantity() + " units of " + product.getProductName() + " left in stock.\n" +
                    "But you're trying to order a quantity of " + quantity + " units.\n" +
                    "If it is a technical error, please, contact the administrator: admin@admin.com");

        }
} catch (InvalidQuantityException e){
    System.out.println(e.getMessage());

}

    }

    //Check if we have any orders to show - not implemented
    public void showOrderDetailsAsATable(Order order) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("+-----------------+---------------------+---------------+------------+------------+\n");
        sb.append("|   Order number  |        Date         |     Product   |  Quantity  |    Price    \n");
        sb.append("+-----------------+---------------------+---------------+------------+------------+\n");
        sb.append(String.format("| %15d | %16s | %13s | %10d |%10.2f \n",
                order.orderNumber, order.orderDate, order.getProductName(), order.getQuantity(), order.getTotalAmount()));
        sb.append("+-----------------+---------------------+---------------+------------+------------+\n");
        sb.toString();
        System.out.println(sb);
    }

    public void showAllCustomersOrdersByID(int id) throws SQLException {
        ArrayList<Order> orders = orderRepository.getAllOrdersByCustomerId(id);
        if (!orders.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("+-----------------+---------------------+-----------------+------------+------------+\n");
            sb.append("|   Order number  |        Date         |      Product    |  Quantity  | Order price \n");
            sb.append("+-----------------+---------------------+-----------------+------------+------------+\n");

            for (Order order : orders) {
                sb.append(String.format("| %15d | %16s | %15s | %10d |%10.2f \n",
                        order.orderNumber,
                        order.orderDate,
                        order.getProductName(),
                        order.getQuantity(),
                        order.getTotalAmount()));
            }
            sb.append("+-----------------+---------------------+-----------------+------------+------------+\n");
            sb.toString();
            System.out.println(sb);
        }else {
            System.out.println("No customers orders found");
        }
    }

    /*public int askQuantity() {
        try {
            System.out.println("Enter the quantity (only whole numbers are acceptable): ");
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e){//in case the user does not enter a number
            return askQuantity();
        }
    }*/

    public int askQuantity() {
        try {
            System.out.println("Enter the quantity: ");
            String input = scanner.nextLine();
            if (input.matches("^-?\\d*\\.\\d+$") || input.matches("^-?\\d*,\\d+$")) {
                System.out.println("Only whole numbers are acceptable. Please try again\n");
                return askQuantity();
            }
            return Integer.parseInt(input);
        } catch (NumberFormatException e) { // in case the user does not enter a number
            System.out.println("Invalid input\n");
            return askQuantity();
        }
    }

    public boolean validQuantity(int quantity, Product product) {
            if (quantity <= 0) {
                System.out.println("Oops... A quantity greater than zero must be entered");
                return false;
            } else if (quantity <= product.getStockQuantity()){
                return true;
            } else {
                System.out.println("There are only " + product.getStockQuantity() + " units of " + product.getProductName() + " left in stock");
                //int newQuantity = askQuantity();
                //return validQuantity(newQuantity, product);
                return false;
            }
    }

    public void processOrder(Product product, Customer customer) throws SQLException {
        int quantity = askQuantity();
        if(validQuantity(quantity, product)){
            createNewOrder(customer, product, quantity);
            productService.reduceStockQuantity(product, quantity);
        } else {
            processOrder(product, customer);//if the quantity is not valid, the method is called again
        }
    }




}

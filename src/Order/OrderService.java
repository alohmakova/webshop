package order;

import customer.Customer;
import product.Product;
import util.InvalidIDException;
import util.InvalidQuantityException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import static order.OrderController.productService;
import static order.OrderController.scanner;

public class OrderService {

    // fields
    OrderRepository orderRepository;

    //constructor initialises the repository layer
    public OrderService() {
        this.orderRepository = new OrderRepository();
    }

    //Check if we have any orders to show - not implemented
    public void showAllCustomersOrdersById(int customerId) throws SQLException {//need to throw SQLException otherwise it will not work
        System.out.println(orderRepository.getAllOrdersByCustomerId(customerId));

    }

    //new order should be created with orderNumber, customerId, orderDate, productName, quantity, totalAmount
    public void newOrder(Customer customer, Product product, int quantity) throws SQLException {
        try {
            if (product.getStockQuantity() >= quantity) {
                Order order = new Order(
                        orderRepository.getMaxOrderId() + 1,      //int customerId
                        customer.getCustomerId(),
                        product.getProductName(),                       //String productName
                        quantity,                                       //int quantity
                        quantity * product.getProductPrice());            //double totalAmount

                //save new order in the database in orders table
                orderRepository.saveOrder(order);
                //save new order in the database in orders_products table
                orderRepository.insertIntoOrdersProducts(order, product);


                System.out.println("Thank you, the order with id " + order.getOrderId() + " has been successfully created!\nOrder details: ");
                //show order details to the customer
                printOrder(order);

            } else {

                throw new InvalidQuantityException("There are only " + product.getStockQuantity() + " units of " + product.getProductName() + " left in stock.\n" +
                        "But you're trying to order a quantity of " + quantity + " units.\n" +
                        "If it is a technical error, please, contact the administrator: admin@admin.com");

            }
        } catch (InvalidQuantityException e) {
            System.err.println(e.getMessage());

        }

    }

    public void showAllCustomersOrdersByID(Customer customer) throws SQLException {
        HashSet<Order> orders = orderRepository.getAllOrdersByCustomerId(customer.getId());
        System.out.println("All orders of the customer with email " + customer.getEmail());
        printOrders(orders);
    }
    public void showLimitedCustomersOrdersByID(int customerId) throws SQLException {
        HashSet<Order> orders = orderRepository.getLimitedOrdersByCustomerId(customerId);
        printOrders(orders);
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
                throw new InvalidQuantityException("Only whole numbers are acceptable. Please try again");
            } else if (input.matches("")) {
                throw new InvalidQuantityException("You pressed enter instead of entering the quantity");//if the user enters an empty string
            }
            return Integer.parseInt(input);
        } catch (NumberFormatException e) { // in case the user does not enter a number
            System.err.println("Invalid input\n");
            return askQuantity();
        } catch (InvalidQuantityException e) {
            System.err.println(e.getMessage());
            return askQuantity();
        }
    }

    public boolean validQuantity(int quantity, Product product) {
        if (quantity <= 0) {
            System.out.println("Oops... A quantity greater than zero must be entered");
            return false;
        } else if (quantity <= product.getStockQuantity()) {
            return true;
        } else {
            System.err.println("There are only " + product.getStockQuantity() + " units of " + product.getProductName() + " left in stock");
            return false;
        }
    }

    public int chooseQuantityFor(Product product) {
        int quantity = askQuantity();
        if (validQuantity(quantity, product)) {
            return quantity;
        } else {
            return chooseQuantityFor(product);//if the quantity is not valid, the method is called again
        }

    }

    public void deleteOrder(int orderId) {
        orderRepository.deleteOrder(orderId);
        System.out.println("Order with id " + orderId + " has been successfully deleted");
    }

    public int chooseOrderId(Scanner scanner, Customer customer) throws SQLException {

        System.out.println("Enter the order id: \n");//ask to choose the order id

        //I get a list of all existing order id's
        HashSet<Order> orders = orderRepository.getAllOrdersByCustomerId(customer.getCustomerId());
        if (orders.isEmpty()) {
            System.err.println("No orders found");
            return 0;
        } else {
            ArrayList<Integer> ordersId = new ArrayList<>();
            orders.forEach(order -> ordersId.add(order.getOrderId()));

            int orderId;


            try {
                String input = scanner.nextLine();
                if (input.matches("")) {//if the user enters an empty string
                    throw new InvalidIDException("You pressed enter instead of entering the id");
                } else {
                orderId = Integer.parseInt(input);//get the order number from the user
                //orderId validation
                if(!ordersId.contains(orderId)){//in case the user enters a number what doesn't exist in the list
                    throw new InvalidIDException("Ooops... provided id doesn't exist in the list🤷‍♂️!");//my own exception
                }
                }
            } catch (NumberFormatException e){//in case the user does not enter a number
                System.err.println("Ooops... provided input doesn't look like order id🤔!");

                return chooseOrderId(scanner, customer);
            } catch (InvalidIDException e) {
                System.err.println(e.getMessage());
                return chooseOrderId(scanner, customer);
            }
            return orderId;
        }

    }

    public void changeOrderQuantity(int customerId, Order order, Product product, int quantity) {
        try {
            if (product.getStockQuantity() >= quantity && quantity != order.getQuantity()) {
                Order oldOrder = new Order(
                        order.getOrderId(),
                        customerId,
                        order.getOrderDate(),//old date
                        product.getProductName(),
                        order.getQuantity(),//old quantity
                        order.getTotalAmount());//old total amount
                Order updatedOrder = new Order(
                        order.getOrderId(),
                        customerId,
                        //here new date will be generated automatically
                        product.getProductName(),
                        quantity,//new quantity from the user
                        quantity * product.getProductPrice());//new total amount
                //save changed order in the database in orders_products and orders tables
                orderRepository.saveChangedOrder(updatedOrder, product, quantity);
                productService.updateStockQuantity(product, quantity, order.getQuantity());


                System.out.println("The order with id " + order.getOrderId() + " has been successfully changed!\nCheck the updated order: ");
                printOrder(orderRepository.getOrderById(order.getOrderId()));
                System.out.println("Compare with the previous version of the order: ");
                printOrder(oldOrder);

            } else if (quantity == order.getQuantity()){
                throw new InvalidQuantityException("You did not change the quantity");// in case the user did not change the quantity, but the quantity is valid
            } else {

                throw new InvalidQuantityException("There are only " + product.getStockQuantity() + " units of " + product.getProductName() + " left in stock.\n" +
                        "But you're trying to order a quantity of " + quantity + " units.\n" +
                        "If it is a technical error, please, contact the administrator: admin@admin.com");

            }
        } catch (NullPointerException | SQLException e) {
            e.getMessage();

        }
    }

    public void changeOrder(Product product, Order order, Customer customer) {
        try {
            int quantity = chooseQuantityFor(product);
            changeOrderQuantity(customer.getCustomerId(), order, product, quantity);
        } catch (InvalidQuantityException e) {
            System.err.println(e.getMessage());
            changeOrder(product, order, customer);
        }

    }

    /**private void showHistoryOfOrderChanges (int orderId) throws SQLException {
        ArrayList<Order> orders = orderRepository.getHistoryOfOrderChanges(orderId);
        printOrders(orders);
    }*/

    public void printOrder(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append("+-------------+---------------------+---------------+------------+------------+\n");
        sb.append("|   Order id  |        Date         |     Product   |  Quantity  | Order price \n");
        sb.append("+-------------+---------------------+---------------+------------+------------+\n");
        sb.append(String.format("| %11d | %16s | %13s | %10d |%10.2f \n",
                order.getOrderId(), order.getOrderDate(), order.getProductName(), order.getQuantity(), order.getTotalAmount()));
        sb.append("+-------------+---------------------+---------------+------------+------------+\n");
        sb.toString();
        System.out.println(sb);
    }

    private void printOrders(HashSet<Order> orders) {
        if (!orders.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("+-------------+---------------------+-----------------+------------+------------+\n");
            sb.append("|   Order id  |        Date         |      Product    |  Quantity  | Order price \n");
            sb.append("+-------------+---------------------+-----------------+------------+------------+\n");

            for (Order order : orders) {
                sb.append(String.format("| %11d | %16s | %15s | %10d |%10.2f \n",
                        order.getOrderId(),
                        order.getOrderDate(),
                        order.getProductName(),
                        order.getQuantity(),
                        order.getTotalAmount()));
            }
            sb.append("+-------------+---------------------+-----------------+------------+------------+\n");
            sb.toString();
            System.out.println(sb);
        } else {
            System.err.println("No orders found");
        }
    }

    private void printOrders(ArrayList<Order> orders) {
        if (!orders.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("+-------------+---------------------+-----------------+------------+------------+\n");
            sb.append("|   Order id  |        Date         |      Product    |  Quantity  | Order price \n");
            sb.append("+-------------+---------------------+-----------------+------------+------------+\n");

            for (Order order : orders) {
                sb.append(String.format("| %11d | %16s | %15s | %10d |%10.2f \n",
                        order.getOrderId(),
                        order.getOrderDate(),
                        order.getProductName(),
                        order.getQuantity(),
                        order.getTotalAmount()));
            }
            sb.append("+-------------+---------------------+-----------------+------------+------------+\n");
            sb.toString();
            System.out.println(sb);
        } else {
            System.err.println("No orders found");
        }
    }



    public Order getOrder(Customer customer) throws SQLException {
        showAllCustomersOrdersByID(customer);//?????I write this line separately
        // so that the table is not displayed again for each incorrect input from the user
        int orderId = chooseOrderId(scanner, customer);
        if (orderRepository.getOrderById(orderId) != null) {
            return orderRepository.getOrderById(orderId);
        } else {
            System.err.println("Order with id " + orderId + " does not exist. Choose another order id");
            return getOrder(customer);
        }
    }
}

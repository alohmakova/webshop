package order;

import customer.Customer;
import filters.OrderFilters;
import product.Product;
import util.BaseLogger;
import util.InvalidIDException;
import util.InvalidQuantityException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;

import static order.OrderController.productService;
import static order.OrderController.scanner;

public class OrderService extends BaseLogger{

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

    //new order should be created with customerId, orderDate (autogenerated), productName, quantity, totalAmount
    public void newOrder(Customer customer, Product product, int quantity) throws SQLException {
        try {
            if (product.getStockQuantity() >= quantity) {
                logger.fine("The user has specified a product quantity less than or equal to the stock quantity: " + quantity);
                Order order = new Order(
                        customer.getCustomerId(),
                        product.getProductName(),                       //String productName
                        quantity,                                       //int quantity
                        quantity * product.getProductPrice());            //double totalAmount
                logger.fine("The order object was created with orderId=0: " + order);
                orderRepository.saveOrder(order);
                logger.fine("The order object was saved in the orders table: orderId was autogenerated, customerId and date were saved from the object");
                ArrayList<Order> orders = orderRepository.getAllOrdersByCustomerId(customer.getCustomerId());
                logger.fine("The order will be retrieved from the database, because I need to know orderId before sending the order to the orders_products table, orderId=: " + orders.getLast().getOrderId());
                orderRepository.insertIntoOrdersProducts(order, orders.getLast(), product);
                logger.fine("The order with id " + orders.getLast().getOrderId() + " was saved in the orders_products table " + orders.getLast());
                System.out.println("Thank you, the order with id " + orders.getLast().getOrderId() + " has been successfully created!\nOrder details: ");
                ArrayList<Order> ordersToPrint = orderRepository.getAllOrdersByCustomerId(customer.getCustomerId());
                logger.fine("The order will be retrieved from the database again to show all order details to the customer correct");
                printOrder(ordersToPrint.getLast());

            } else {
                logger.fine("The method newOrder(Customer customer, Product product, int quantity) receives NOT that quantity that was in user's input - to look for an error in the code");
                throw new InvalidQuantityException("There are only " + product.getStockQuantity() + " units of " + product.getProductName() + " left in stock.\n" +
                        "But you're trying to order a quantity of " + quantity + " units.\n" +
                        "If it is a technical error, please, contact the administrator: admin@admin.com");

            }
        } catch (InvalidQuantityException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);

        }

    }

    public ArrayList<Order> showAllCustomersOrdersByID(Customer customer) throws SQLException {
        ArrayList<Order> orders = orderRepository.getAllOrdersByCustomerId(customer.getId());
        logger.fine("A ArrayList<Order> orders was created to store all customer orders retrieved from the database");
        if (orders.isEmpty()) {
            logger.warning("ArrayList<Order> orders is empty for the customer with email " + customer.getEmail());
            System.out.println("No orders were found for the customer with email " + customer.getEmail());
        } else {
            System.out.println("All orders of the customer with email " + customer.getEmail());
            printOrders(orders);
            logger.info("The system shows all customer orders by customer's id");
        }
        return orders;
    }
    public void showLimitedCustomersOrdersByID(int customerId) throws SQLException {
        ArrayList<Order> orders = orderRepository.getLimitedOrdersByCustomerId(customerId);
        logger.fine("A ArrayList<Order> orders was created to store last 10 customer orders retrieved from the database");
        if (orders.isEmpty()) {
            logger.warning("A ArrayList<Order> orders is empty for the customer with with id " + customerId);
            System.out.println("No orders were found ");

        } else {
            System.out.println("Your last orders (10 orders limit) ");
            printOrders(orders);
            logger.info("The system shows last 10 customer orders by customer's id");
        }
    }

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

    public void deleteOrder(int orderId, Customer customer) throws SQLException {
            orderRepository.deleteOrder(orderId);
            logger.info("Sent sql query to delete an order with id " + orderId);
            System.out.println("Order with id " + orderId + " has been successfully deleted");
            logger.info("Order with id " + orderId + " has been successfully deleted from the database");
            showAllCustomersOrdersByID(customer);
    }

    public int chooseOrderId(Customer customer, ArrayList<Order> orders) throws SQLException {
        int orderId;
        try {
            System.out.println("Enter the order id: \n");
            String input = scanner.nextLine();//get the order number from the user
            logger.info("From all the customer orders, the user selects the order by specifying its id");
            if (input.matches("")) {//if the user enters an empty string
                logger.warning("User entered an empty string, the InvalidIDException will be thrown");
                throw new InvalidIDException("You pressed enter instead of entering the id");
            } else {
            orderId = Integer.parseInt(input);
                logger.fine("Attempting to convert the user input from String into int");
            //orderId validation
                ArrayList<Integer> ordersId = new ArrayList<>();
                logger.fine("ArrayList<Integer> ordersId was created to store all existing order id's of the customer");
                orders.forEach(order -> ordersId.add(order.getOrderId()));
                logger.fine("All existing order id's have been added to ArrayList<Integer> ordersId");
            if(!ordersId.contains(orderId)){//in case the user enters a number what doesn't exist in the list
                logger.warning("User entered a number that doesn't exist in the ArrayList<Integer> ordersId, the InvalidIDException will be thrown");
                throw new InvalidIDException("Ooops... provided id doesn't exist in the list🤷‍♂️!");//my own exception
            }
            }
        } catch (NumberFormatException e){
            logger.warning("User entered not enter a number, the NumberFormatException was caught, the user will be prompted to re-select the id");
            System.err.println("Ooops... provided input doesn't look like order id🤔!");//in case the user does not enter a number
            return chooseOrderId(customer, orders);
        } catch (InvalidIDException e) {
            logger.warning("The InvalidIDException was caught, the user will be prompted to re-select the id");
            System.err.println(e.getMessage());
            return chooseOrderId(customer, orders);
        }
        logger.info("The user has successfully selected the order with id " + orderId);
        return orderId;
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
        if (order != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("+-------------+---------------------+---------------+------------+------------+\n");
            sb.append("|   Order id  |        Date         |     Product   |  Quantity  | Order price \n");
            sb.append("+-------------+---------------------+---------------+------------+------------+\n");
            sb.append(String.format("| %11d | %16s | %13s | %10d |%10.2f \n",
                    order.getOrderId(), order.getOrderDate(), order.getProductName(), order.getQuantity(), order.getTotalAmount()));
            sb.append("+-------------+---------------------+---------------+------------+------------+\n");
            sb.toString();
            System.out.println(sb);
            logger.fine("An object order was printed as a table\n" + sb);
        } else {
            throw new NullPointerException("The order with id " + order.getOrderId() + " was not found");
        }
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
            logger.fine("A HashSet<Order> orders was printed as a table\n" + sb);
        } else {
            System.out.println("No orders found");
            logger.warning("A HashSet<Order> orders was empty");
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
            System.out.println("No orders found");
            logger.warning("An ArrayList<Order> orders was empty");
        }
    }



    public Order chooseOrder(Customer customer, ArrayList<Order> orders) throws SQLException {
        int orderId = chooseOrderId(customer, orders);
        if (orderRepository.getOrderById(orderId) != null) {
            logger.warning("The order object was created for the order with id " + orderId);
            return orderRepository.getOrderById(orderId);
        } else {
            System.out.println("Order with id " + orderId + " does not exist. Choose another order id");
            logger.warning("The object order was not found, user will be promoted to choose atother order id");
            return chooseOrder(customer, orders);
        }
    }

    public void productNameFilter(Customer customer) throws SQLException {
        ArrayList<Order> orders = orderRepository.getAllOrdersByCustomerId(customer.getCustomerId());
        OrderFilters orderFilters = new OrderFilters();
        System.out.println("Enter the product name to filter orders: ");
        String searchString = scanner.nextLine();
        //check for empty string protects against incorrect input from the user
        if(searchString.matches("") || searchString.matches(" ")){
            System.out.println("Invalid input. Please, try again");
            productNameFilter(customer);
            return;
        }
        ArrayList<Order> filteredOrders = orderFilters.filterOrdersByProductName(orders, searchString);
        printOrders(filteredOrders);
    }


}

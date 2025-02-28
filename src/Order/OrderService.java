package order;

import customer.Customer;
import customer.CustomerRepository;
import product.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class OrderService {

    // fields
    OrderRepository orderRepository;

    //constructor initialises the repository layer
    public OrderService() {
        this.orderRepository = new OrderRepository();
    }

    //Check if we have any orders to show - not implemented
    public void showAllCustomersOrdersById(int customerId) throws SQLException {//need to throw SQLException otherwise it will not work
        System.out.println(orderRepository.getAllOrdersbyCustomerId(customerId));

    }

    //new order should be created with orderNumber, customerId, orderDate, productName, quantity, totalAmount
    public void createNewOrder(Customer customer, Product product, int quantity) throws SQLException {
        CustomerRepository customerRepository = new CustomerRepository();
        Order order = new Order(
                orderRepository.getMaxOrderId()+1,//int orderNumber
                customer.getCustomerId(),//int customerId
                //customerRepository.getCustomerIdByEmail(email),
                product.getProductName(),                       //String productName
                quantity,                                       //int quantity
                quantity*product.getProductPrice());            //double totalAmount
        //save new order in the database in orders table
        orderRepository.addOrder(order);
        //save new order in the database in orders_products table
        orderRepository.insertIntoOrdersProducts(order, product);
        //reduce the stock quantity of products when creating an order

        System.out.println("Thank you, order with the number " + order.orderNumber +" has been successfully created!\nOrder details: ");
        //show order details to the customer
        showOrderDetailsAsATable(order);
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
        ArrayList<Order> orders = orderRepository.getAllOrdersbyCustomerId(id);
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

    public boolean validateQuantity(int quantity, Product product, Customer customer, Scanner scanner) throws SQLException {
        while (true) {
            if (quantity == 0) {
                System.out.println("Oops... A quantity greater than zero must be entered");
                System.out.println("Enter the quantity:\n");
                quantity = scanner.nextInt();//validate that iser's input is int
            } else if (quantity <= product.getStockQuantity()){
                createNewOrder(customer, product, quantity);
                return false;
            } else {
                System.out.println("There are only " + product.getStockQuantity() + " units of " + product.getProductName() + " left in stock");
                return false;
            }
        }
    }


}

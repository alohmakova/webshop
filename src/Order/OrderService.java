package order;

import customer.CustomerRepository;
import product.Product;

import java.sql.SQLException;

public class OrderService {

    // fields
    OrderRepository orderRepository;

    //constructor initialises the repository layer
    public OrderService() {
        this.orderRepository = new OrderRepository();
    }

    //create methods for menu
    public void showAllCustomersOrdersById(int customerId) throws SQLException {//need to throw SQLException otherwise it will not work
        System.out.println(orderRepository.getAllOrdersbyCustomerId(customerId));

    }

    //new order should be created with orderNumber, customerId, orderDate, productName, quantity, totalAmount
    public void createNewOrder(String email, Product product, int quantity) throws SQLException {
        CustomerRepository customerRepository = new CustomerRepository();
        Order order = new Order(
                orderRepository.getMaxOrderId()+1,   //int orderNumber
                customerRepository.getCustomerIdByEmail(email), //int customerId
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




}

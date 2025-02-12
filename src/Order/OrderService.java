package order;

import customer.CustomerRepository;

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
    public void createNewOrder(String email) throws SQLException {
        CustomerRepository customerRepository = new CustomerRepository();
        //orderId is created automatically, need to know which id is the last, use getMaxOrderId() from OrderRepository class
        //customerId need to know who is customer, maybe, need to log in. Use getCustomerIdByEmail from CustomerRepository class
        //orderDate = current date and time
        Order order = new Order(orderRepository.getMaxOrderId()+1, customerRepository.getCustomerIdByEmail(email));
        System.out.println(order);

    }




}

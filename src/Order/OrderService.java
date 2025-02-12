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
        Order order = new Order(orderRepository.getMaxOrderId()+1, customerRepository.getCustomerIdByEmail(email));
        //save new order in the database
        orderRepository.addOrder(order);
        System.out.println("Thank you! Your order №" + order.getOrderNumber() +" has been successfully created");
    }




}

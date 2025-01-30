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
    public void createNewOrder(){

    }
}

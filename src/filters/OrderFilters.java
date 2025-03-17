package filters;

import order.Order;
import order.OrderRepository;

import java.util.ArrayList;

public class OrderFilters extends Filters<Order> {

    // fields
    OrderRepository orderRepository;

    //constructor initialises the repository layer
    public OrderFilters() {
        this.orderRepository = new OrderRepository();
    }

    //methods
    public ArrayList<Order> filterOrdersByProductName(ArrayList<Order> orders, String searchString) {
        return (ArrayList<Order>) filterByName(orders, searchString, Order::getProductName);//calls method filterByName() from FilterService
    }
}

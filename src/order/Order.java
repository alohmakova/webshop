package order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Random;

public class Order {

    //fields
    private int orderId;
    private int customerId;
    private String orderDate;
    private String productName;
    private int quantity;
    private double totalAmount;//quantity*unit price

    public Order (int orderId, int customerId, String productName, int quantity, double totalAmount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.productName = productName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }

    public Order(int orderId, int customerId, String orderDate, String productName, int quantity, double totalAmount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.quantity = quantity;
        this.productName = productName;
        this.totalAmount = totalAmount;
    }

    //getters
    public int getOrderId() {
        return orderId;
    }
    public int getCustomerId() {
        return customerId;
    }
    public String getOrderDate() {
        return orderDate;
    }
    public String getProductName() {
        return productName;
    }
    public int getQuantity() {
        return quantity;
    }
    public double getTotalAmount() {
        return totalAmount;
    }

    @Override
    public String toString() {
        return "Order{" +
                ", orderId=" + orderId +
                ", customerId=" + customerId +
                ", orderDate='" + orderDate + '\'' +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", totalAmount=" + totalAmount +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderId == order.orderId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }
}

package order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Order {

    //fields
    private int orderId;
    private int customerId;
    private String orderDate;//I have changed the datatype from Date to String to avoid problems while setting current date and time as orderDate
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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String setOrderDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
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
}

package order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Order {

    //fields
    int orderNumber;
    int customerId;
    String orderDate;//I have changed the datatype from Date to String to avoid problems while setting current date and time as orderDate
    String productName;
    int quantity;
    double totalAmount;//quantity*unit price

    public Order (int orderNumber, int customerId, String productName, int quantity, double totalAmount) {
        this.orderNumber = orderNumber;
        this.customerId = customerId;
        this.orderDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));;
        this.productName = productName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }

    //constructor to show the order with the details only from orders table
    public Order (int orderNumber, int customerId, String orderDate) {
        this.orderNumber = orderNumber;
        this.customerId = customerId;
        this.orderDate = orderDate;
    }

    public Order(int orderId, int customerId, String orderDate, String name, int quantity, double totalAmount) {
        this.orderNumber = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.productName = name;
        this.quantity = quantity;
        this.totalAmount = totalAmount;

    }

    //method to set the current time for orderDate, do not use it, because put it directly in the constructor
    public String setCurrentDateAsOrderDate() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        return formattedTime;
    }

    //getters and setters

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    //to String - think how to represent the information better
    @Override
    public String toString() {
        return "Order{" +
                "orderNumber=" + orderNumber +
                ", customerId=" + customerId +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                '}';
    }


}

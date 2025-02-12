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
    double totalAmount;

    //constructor to get the order from db
    public Order (int orderNumber, int customerId, String orderDate){
        this.orderNumber = orderNumber;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.totalAmount = 0;
    }

    //constructor to put the order to db
    public Order (int orderNumber, int customerId){
        this.orderNumber = orderNumber;
        this.customerId = customerId;
        this.orderDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));//orderDate - current date and time
        this.totalAmount = 0;
    }

    //method to set the current time for orderDate, do not use it, because put it directly in the constructor
    public String setCurrentDateAsOrderDate() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        return formattedTime;
    }

    //getters
    public int getOrderNumber() {
        return orderNumber;
    }
    public int getCustomerId() {
        return customerId;
    }
    public String getOrderDate() {
        return orderDate;
    }
    public double getTotalAmount() {
        return totalAmount;
    }

    //setters
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public void setOrderDate(String orderDate) {this.orderDate = orderDate;}
    public void setTotalAmount(double totalAmount) {this.totalAmount = totalAmount;}


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

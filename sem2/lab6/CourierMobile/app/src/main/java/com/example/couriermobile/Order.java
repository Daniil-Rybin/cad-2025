
package com.example.couriermobile;

public class Order {

    private Long id;
    private String clientName;
    private String pickupAddress;
    private String deliveryAddress;
    private String description;
    private String category;
    private double weight;
    private String status;
    private String courierName;

    public Long getId() {
        return id;
    }

    public String getClientName() {
        return clientName;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public double getWeight() {
        return weight;
    }

    public String getStatus() {
        return status;
    }

    public String getCourierName() {
        return courierName;
    }
}

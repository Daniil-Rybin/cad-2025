package com.example.courierservice.model;

public class DeliveryOrder {
    private static Long nextId = 1L;

    private Long id;
    private String clientName;
    private String pickupAddress;
    private String deliveryAddress;
    private String courierName;
    private String description;
    private String category;
    private double weight;
    private String status;

    public DeliveryOrder() {
        this.id = generateId();
        this.status = "NEW";
        this.courierName = "Не назначен";
    }

    public DeliveryOrder(String clientName, String pickupAddress,
                         String deliveryAddress, String description,
                         String category, double weight) {
        this.id = generateId();
        this.clientName = clientName;
        this.pickupAddress = pickupAddress;
        this.deliveryAddress = deliveryAddress;
        this.description = description;
        this.category = category;
        this.weight = weight;
        this.status = "NEW";
        this.courierName = "Не назначен";
    }

    private synchronized Long generateId() {
        return nextId++;
    }

    public Long getId() { return id; }
    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getCourierName() { return courierName; }
    public void setCourierName(String courierName) { this.courierName = courierName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "DeliveryOrder{" +
                "id=" + id +
                ", clientName='" + clientName + '\'' +
                ", pickupAddress='" + pickupAddress + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", courierName='" + courierName + '\'' +
                ", category='" + category + '\'' +
                ", weight=" + weight +
                ", status='" + status + '\'' +
                '}';
    }
}
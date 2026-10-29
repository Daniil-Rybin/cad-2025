package com.example.courierservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_orders")
public class DeliveryOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;
    private String clientName;
    private String pickupAddress;
    private String deliveryAddress;
    private String courierName;
    private String description;
    private String category;
    private double weight;
    private String status;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    public DeliveryOrder() {
        this.status = "NEW";
        this.courierName = "Не назначен";
        this.createdAt = LocalDateTime.now();
    }

    public DeliveryOrder(String clientName, String pickupAddress,
                         String deliveryAddress, String description,
                         String category, double weight) {
        this.clientName = clientName;
        this.pickupAddress = pickupAddress;
        this.deliveryAddress = deliveryAddress;
        this.description = description;
        this.category = category;
        this.weight = weight;
        this.status = "NEW";
        this.courierName = "Не назначен";
        this.createdAt = LocalDateTime.now();
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

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

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
}
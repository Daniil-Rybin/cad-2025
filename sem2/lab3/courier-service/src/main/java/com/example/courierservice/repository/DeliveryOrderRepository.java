package com.example.courierservice.repository;

import com.example.courierservice.model.DeliveryOrder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DeliveryOrderRepository {

    private final List<DeliveryOrder> orders = new ArrayList<>();

    public void addOrder(DeliveryOrder order) {
        orders.add(order);
    }

    public List<DeliveryOrder> getAllOrders() {
        return orders;
    }

    public Optional<DeliveryOrder> findById(Long id) {
        return orders.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst();
    }

    public void deleteOrder(Long id) {
        orders.removeIf(o -> o.getId().equals(id));
    }
}
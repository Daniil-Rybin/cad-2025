package com.example.courierservice.service;

import com.example.courierservice.model.DeliveryOrder;
import com.example.courierservice.repository.DeliveryOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeliveryOrderService {

    private final DeliveryOrderRepository repository;

    @Autowired
    public DeliveryOrderService(DeliveryOrderRepository repository) {
        this.repository = repository;
    }

    public void addOrder(DeliveryOrder order) {
        repository.addOrder(order);
    }

    public List<DeliveryOrder> getAllOrders() {
        return repository.getAllOrders();
    }

    public Optional<DeliveryOrder> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteOrder(Long id) {
        repository.deleteOrder(id);
    }

    public void updateStatus(Long id, String status) {
        repository.findById(id).ifPresent(o -> o.setStatus(status));
    }

    public void assignCourier(Long id, String courierName) {
        repository.findById(id).ifPresent(o -> o.setCourierName(courierName));
    }

    public List<DeliveryOrder> filterByCategory(String category) {
        return repository.getAllOrders().stream()
                .filter(o -> category.equals(o.getCategory()))
                .collect(Collectors.toList());
    }

    public List<DeliveryOrder> search(String query) {
        String q = query.toLowerCase();
        return repository.getAllOrders().stream()
                .filter(o -> o.getClientName().toLowerCase().contains(q)
                        || o.getPickupAddress().toLowerCase().contains(q)
                        || o.getDeliveryAddress().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }
}
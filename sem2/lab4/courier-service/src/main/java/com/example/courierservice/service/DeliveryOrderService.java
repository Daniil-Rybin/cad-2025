package com.example.courierservice.service;

import com.example.courierservice.model.DeliveryOrder;
import com.example.courierservice.model.OrderHistory;
import com.example.courierservice.repository.DeliveryOrderRepository;
import com.example.courierservice.repository.OrderHistoryRepository;
import com.example.courierservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryOrderService {

    private final DeliveryOrderRepository repository;
    private final OrderHistoryRepository historyRepository;
    private final UserRepository userRepository;

    public DeliveryOrderService(DeliveryOrderRepository repository,
                                OrderHistoryRepository historyRepository,
                                UserRepository userRepository) {
        this.repository = repository;
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void addOrder(DeliveryOrder order, String username) {
        userRepository.findByUsername(username).ifPresent(order::setCreatedBy);
        repository.save(order);
    }

    public List<DeliveryOrder> getAllOrders() {
        return repository.findAll();
    }

    public Optional<DeliveryOrder> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteOrder(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void updateStatus(Long id, String newStatus, String changedBy) {
        repository.findById(id).ifPresent(o -> {
            String oldStatus = o.getStatus();
            o.setStatus(newStatus);
            historyRepository.save(new OrderHistory(o, oldStatus, newStatus, changedBy));
        });
    }

    @Transactional
    public void assignCourier(Long id, String courierName) {
        repository.findById(id).ifPresent(o -> o.setCourierName(courierName));
    }

    public List<DeliveryOrder> filterByCategory(String category) {
        return repository.findByCategory(category);
    }

    public List<DeliveryOrder> search(String query) {
        return repository
                .findByClientNameContainingIgnoreCaseOrPickupAddressContainingIgnoreCaseOrDeliveryAddressContainingIgnoreCase(
                        query, query, query);
    }

    public List<OrderHistory> getHistory(Long orderId) {
        return historyRepository.findByOrderIdOrderByChangedAtDesc(orderId);
    }
}
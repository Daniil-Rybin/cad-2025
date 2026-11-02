package com.example.courierservice.repository;

import com.example.courierservice.model.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    List<OrderHistory> findByOrderIdOrderByChangedAtDesc(Long orderId);
}
package com.example.courierservice.repository;

import com.example.courierservice.model.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, Long> {

    List<DeliveryOrder> findByCategory(String category);

    List<DeliveryOrder> findByClientNameContainingIgnoreCaseOrPickupAddressContainingIgnoreCaseOrDeliveryAddressContainingIgnoreCase(
            String clientName, String pickupAddress, String deliveryAddress);
}
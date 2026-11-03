
package com.example.courierservice.controller;

import com.example.courierservice.model.DeliveryOrder;
import com.example.courierservice.service.DeliveryOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class DeliveryOrderApiController {

    private final DeliveryOrderService service;

    public DeliveryOrderApiController(DeliveryOrderService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'COURIER', 'MODERATOR')")
    public List<OrderResponse> getOrders() {
        return service.getAllOrders().stream()
                .map(OrderResponse::from)
                .toList();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'COURIER', 'MODERATOR')")
    public ResponseEntity<?> addOrder(
            @RequestBody OrderRequest request,
            Authentication auth) {

        if (request.clientName() == null ||
                request.clientName().isBlank() ||
                request.pickupAddress() == null ||
                request.pickupAddress().isBlank() ||
                request.deliveryAddress() == null ||
                request.deliveryAddress().isBlank() ||
                request.weight() <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Некорректные данные заявки"));
        }

        DeliveryOrder order = new DeliveryOrder(
                request.clientName(),
                request.pickupAddress(),
                request.deliveryAddress(),
                request.description(),
                request.category(),
                request.weight()
        );

        service.addOrder(order, auth.getName());
        return ResponseEntity.ok(Map.of("message", "Заявка создана"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    public record OrderRequest(
            String clientName,
            String pickupAddress,
            String deliveryAddress,
            String description,
            String category,
            double weight
    ) {}

    public record OrderResponse(
            Long id,
            String clientName,
            String pickupAddress,
            String deliveryAddress,
            String description,
            String category,
            double weight,
            String status,
            String courierName
    ) {
        public static OrderResponse from(DeliveryOrder order) {
            return new OrderResponse(
                    order.getId(),
                    order.getClientName(),
                    order.getPickupAddress(),
                    order.getDeliveryAddress(),
                    order.getDescription(),
                    order.getCategory(),
                    order.getWeight(),
                    order.getStatus(),
                    order.getCourierName()
            );
        }
    }
}

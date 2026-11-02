package com.example.courierservice.controller;

import com.example.courierservice.model.DeliveryOrder;
import com.example.courierservice.service.DeliveryOrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DeliveryOrderController {

    private final DeliveryOrderService service;

    public DeliveryOrderController(DeliveryOrderService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("orders", service.getAllOrders());
        return "index";
    }

    @PreAuthorize("hasAnyRole('USER', 'COURIER', 'MODERATOR')")
    @PostMapping("/addOrder")
    public String addOrder(@ModelAttribute DeliveryOrder order, Authentication auth) {
        service.addOrder(order, auth.getName());
        return "redirect:/";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/deleteOrder/{id}")
    public String deleteOrder(@PathVariable Long id) {
        service.deleteOrder(id);
        return "redirect:/";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/updateStatus/{id}")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               Authentication auth) {
        service.updateStatus(id, status, auth.getName());
        return "redirect:/";
    }

    @PreAuthorize("hasAnyRole('COURIER', 'MODERATOR')")
    @PostMapping("/assignCourier/{id}")
    public String assignCourier(@PathVariable Long id,
                                @RequestParam String courierName) {
        service.assignCourier(id, courierName);
        return "redirect:/";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/filter")
    public String filter(@RequestParam String category, Model model) {
        model.addAttribute("orders", service.filterByCategory(category));
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam String query, Model model) {
        model.addAttribute("orders", service.search(query));
        return "index";
    }

    @GetMapping("/order/{id}/history")
    public String orderHistory(@PathVariable Long id, Model model) {
        model.addAttribute("order", service.findById(id).orElseThrow());
        model.addAttribute("history", service.getHistory(id));
        return "order-history";
    }
}
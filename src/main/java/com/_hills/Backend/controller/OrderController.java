package com._hills.Backend.controller;

import com._hills.Backend.entity.Order;
import com._hills.Backend.entity.Vendor;
import com._hills.Backend.repository.OrderRepository;
import com._hills.Backend.service.AdminService;
import com._hills.Backend.service.VendorManager;
import com._hills.Backend.util.AppConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final AdminService adminService;
    private final VendorManager vendorManager;

    public OrderController(OrderRepository orderRepository, AdminService adminService, VendorManager vendorManager) {
        this.orderRepository = orderRepository;
        this.adminService = adminService;
        this.vendorManager = vendorManager;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findAll(pageable);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", orders.getContent(),
                "totalElements", orders.getTotalElements(),
                "totalPages", orders.getTotalPages(),
                "currentPage", orders.getNumber(),
                "pageSize", orders.getSize()
        ));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Map<String, Object>> getOrderById(@PathVariable Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", order
        ));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> getOrdersByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findByStatus(status, pageable);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", orders.getContent(),
                "totalElements", orders.getTotalElements(),
                "totalPages", orders.getTotalPages()
        ));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody Map<String, String> request) {
        String status = request.get("status");
        Order order = vendorManager.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Order status updated to " + status,
                "data", order
        ));
    }

    @PutMapping("/{orderId}/assign-vendor")
    public ResponseEntity<Map<String, Object>> assignVendor(
            @PathVariable Long orderId,
            @RequestBody Map<String, Object> request) {
        Long vendorId = ((Number) request.get("vendorId")).longValue();
        Long adminId = request.get("adminId") != null ? ((Number) request.get("adminId")).longValue() : 1L;
        String remarks = (String) request.getOrDefault("adminRemarks", "");
        Order order = vendorManager.assignOrderToVendor(orderId, vendorId, adminId, remarks);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Vendor assigned successfully",
                "data", order
        ));
    }

    @GetMapping("/{orderId}/available-vendors")
    public ResponseEntity<Map<String, Object>> findAvailableVendors(@PathVariable Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            Long productId = order.getItems().get(0).getProduct().getId();
            List<Vendor> vendors = vendorManager.getBestVendorsForProduct(productId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Available vendors found",
                    "data", vendors
            ));
        }
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "No items in order",
                "data", List.of()
        ));
    }
}

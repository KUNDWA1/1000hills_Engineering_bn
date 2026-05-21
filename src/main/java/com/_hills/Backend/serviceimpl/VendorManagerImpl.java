package com._hills.Backend.serviceimpl;

import com._hills.Backend.dto.AdminResponse;
import com._hills.Backend.entity.*;
import com._hills.Backend.exception.ResourceNotFoundException;
import com._hills.Backend.repository.*;
import com._hills.Backend.service.ProductService;
import com._hills.Backend.service.VendorManager;
import com._hills.Backend.util.AppConstants;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VendorManagerImpl implements VendorManager {

    private final VendorRepository vendorRepository;
    private final ProductRepository productRepository;
    private final com._hills.Backend.repository.OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;

    public VendorManagerImpl(VendorRepository vendorRepository,
                             ProductRepository productRepository,
                             com._hills.Backend.repository.OrderRepository orderRepository,
                             DeliveryRepository deliveryRepository, ProductService productService) {
        this.vendorRepository = vendorRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    @Transactional
    public List<Vendor> findAvailableVendorsForProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        List<Product> allProductMatches = productRepository.findByStatusAndVendorId(
                AppConstants.STATUS_APPROVED, product.getVendor().getId(),
                org.springframework.data.domain.Pageable.unpaged()).getContent();
        return vendorRepository.findAll().stream()
                .filter(Vendor::isActive)
                .filter(v -> AppConstants.STATUS_APPROVED.equalsIgnoreCase(v.getStatus()))
                .filter(v -> "AVAILABLE".equalsIgnoreCase(v.getAvailability()))
                .toList();
    }

    @Override
    @Transactional
    public List<Vendor> getBestVendorsForProduct(Long productId) {
        return findAvailableVendorsForProduct(productId).stream()
                .sorted((v1, v2) -> {
                    int cmp = Boolean.compare("AVAILABLE".equalsIgnoreCase(v2.getAvailability()),
                            "AVAILABLE".equalsIgnoreCase(v1.getAvailability()));
                    if (cmp != 0) return cmp;
                    return v1.getCompanyName().compareToIgnoreCase(v2.getCompanyName());
                })
                .toList();
    }

    @Override
    @Transactional
    public Order assignOrderToVendor(Long orderId, Long vendorId, Long adminId, String adminRemarks) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + vendorId));

        order.setAssignedVendor(vendor);
        order.setStatus(AppConstants.STATUS_ASSIGNED);
        if (adminRemarks != null)
            order.setAdminRemarks(adminRemarks);
        orderRepository.save(order);
        return order;
    }

    @Override
    @Transactional
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.setStatus(status);
        if (AppConstants.STATUS_DELIVERED.equalsIgnoreCase(status)) {
            order.setDeliveredAt(java.time.LocalDateTime.now());
        }
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Delivery createDelivery(Long orderId, String deliveryAddress, String courierName) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        Delivery delivery = new Delivery(order);
        delivery.setDeliveryAddress(deliveryAddress);
        delivery.setCourierName(courierName);
        delivery.setStatus(AppConstants.STATUS_PENDING);
        return deliveryRepository.save(delivery);
    }

    @Override
    @Transactional
    public Delivery updateDeliveryStatus(Long deliveryId, String status) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + deliveryId));
        delivery.setStatus(status);
        return deliveryRepository.save(delivery);
    }

    @Override
    @Transactional
    public Order assignAdminToQuotation(Long quotationId, Long vendorId, Long adminId) {
        return null;
    }
}

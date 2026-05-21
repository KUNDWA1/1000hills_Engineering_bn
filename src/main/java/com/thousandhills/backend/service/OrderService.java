package com.thousandhills.backend.service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.thousandhills.backend.dto.OrderItemRequest;
import com.thousandhills.backend.dto.OrderRequest;
import com.thousandhills.backend.model.CustomerProfile;
import com.thousandhills.backend.model.Order;
import com.thousandhills.backend.model.OrderItem;
import com.thousandhills.backend.model.Product;
import com.thousandhills.backend.model.VendorProfile;
import com.thousandhills.backend.repository.OrderRepository;
import com.thousandhills.backend.repository.ProductRepository;
import com.thousandhills.backend.service.EmailNotificationService;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final EmailNotificationService emailNotificationService;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, EmailNotificationService emailNotificationService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.emailNotificationService = emailNotificationService;
    }

    public Order createOrder(CustomerProfile customer, OrderRequest request) {
        Order order = new Order();
        order.setOrderReference("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setCustomer(customer);
        order.setCreatedAt(Instant.now());

        HashSet<OrderItem> items = request.getItems().stream().map(itemRequest -> {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + itemRequest.getProductId()));

            if (product.getStockQuantity() == null || product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product " + product.getTitle());
            }

            product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            return orderItem;
        }).collect(Collectors.toCollection(HashSet::new));

        order.setItems(items);
        double total = items.stream().mapToDouble(item -> item.getUnitPrice() * item.getQuantity()).sum();
        order.setTotalAmount(total);

        return orderRepository.save(order);
    }

    public List<Order> findOrdersByCustomer(CustomerProfile customer) {
        return orderRepository.findByCustomer(customer);
    }

    public List<Order> findOrdersByVendor(VendorProfile vendor) {
        return orderRepository.findByVendor(vendor);
    }

    public Optional<Order> assignVendor(Long orderId, VendorProfile vendor) {
        return orderRepository.findById(orderId).map(order -> {
            order.setVendor(vendor);
            order.setStatus(com.thousandhills.backend.model.OrderStatus.ASSIGNED_TO_VENDOR);
            Order saved = orderRepository.save(order);
            emailNotificationService.notifyOrderAssigned(saved);
            return saved;
        });
    }
}

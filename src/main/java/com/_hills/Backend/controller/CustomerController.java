package com._hills.Backend.controller;

import com._hills.Backend.entity.User;
import com._hills.Backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/customers")
public class CustomerController {

    private final UserRepository userRepository;

    public CustomerController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> customers = userRepository.findAll(pageable);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", customers.getContent(),
                "totalElements", customers.getTotalElements(),
                "totalPages", customers.getTotalPages(),
                "currentPage", customers.getNumber(),
                "pageSize", customers.getSize()
        ));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Map<String, Object>> getCustomerById(@PathVariable Long customerId) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", customer
        ));
    }
}

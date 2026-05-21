package com.thousandhills.backend.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.thousandhills.backend.dto.OrderRequest;
import com.thousandhills.backend.dto.QuotationRequestCreate;
import com.thousandhills.backend.model.AppUser;
import com.thousandhills.backend.model.CustomerProfile;
import com.thousandhills.backend.model.Order;
import com.thousandhills.backend.model.QuotationRequest;
import com.thousandhills.backend.repository.CustomerProfileRepository;
import com.thousandhills.backend.service.AppUserService;
import com.thousandhills.backend.service.OrderService;
import com.thousandhills.backend.service.QuotationService;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerProfileRepository customerProfileRepository;
    private final OrderService orderService;
    private final QuotationService quotationService;
    private final AppUserService appUserService;

    public CustomerController(CustomerProfileRepository customerProfileRepository, OrderService orderService, QuotationService quotationService, AppUserService appUserService) {
        this.customerProfileRepository = customerProfileRepository;
        this.orderService = orderService;
        this.quotationService = quotationService;
        this.appUserService = appUserService;
    }

    @PostMapping("/orders")
    public ResponseEntity<Object> createOrder(Principal principal, @RequestBody OrderRequest orderRequest) {
        return resolveCustomer(principal)
                .map(customer -> ResponseEntity.<Object>ok(orderService.createOrder(customer, orderRequest)))
                .orElse(ResponseEntity.badRequest().body("Customer profile not found."));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getOrders(Principal principal) {
        return resolveCustomer(principal)
                .map(customer -> ResponseEntity.ok(orderService.findOrdersByCustomer(customer)))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/quotations")
    public ResponseEntity<Object> createQuotation(Principal principal, @RequestBody QuotationRequestCreate request) {
        return resolveCustomer(principal)
                .map(customer -> ResponseEntity.<Object>ok(quotationService.createQuotation(customer, request)))
                .orElse(ResponseEntity.badRequest().body("Customer profile not found."));
    }

    @GetMapping("/quotations")
    public ResponseEntity<List<QuotationRequest>> getQuotations(Principal principal) {
        return resolveCustomer(principal)
                .map(customer -> ResponseEntity.ok(quotationService.getCustomerQuotations(customer)))
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Principal principal) {
        return resolveCustomer(principal)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().body("Customer profile not found."));
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(Principal principal, @RequestBody CustomerProfileRequest request) {
        return resolveCustomer(principal)
                .map(customer -> {
                    customer.setFullName(request.getFullName());
                    customer.setPhoneNumber(request.getPhoneNumber());
                    customer.setCompanyName(request.getCompanyName());
                    customer.setAddress(request.getAddress());
                    CustomerProfile updated = customerProfileRepository.save(customer);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.badRequest().body("Customer profile not found."));
    }

    private java.util.Optional<CustomerProfile> resolveCustomer(Principal principal) {
        return appUserService.findByUsername(principal.getName())
                .flatMap(user -> customerProfileRepository.findByUserId(user.getId()));
    }
}

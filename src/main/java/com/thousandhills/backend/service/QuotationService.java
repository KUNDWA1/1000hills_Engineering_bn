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
import com.thousandhills.backend.dto.QuotationRequestCreate;
import com.thousandhills.backend.model.CustomerProfile;
import com.thousandhills.backend.model.Product;
import com.thousandhills.backend.model.QuotationItem;
import com.thousandhills.backend.model.QuotationRequest;
import com.thousandhills.backend.model.QuotationStatus;
import com.thousandhills.backend.model.VendorProfile;
import com.thousandhills.backend.repository.ProductRepository;
import com.thousandhills.backend.repository.QuotationRequestRepository;
import com.thousandhills.backend.service.EmailNotificationService;

@Service
@Transactional
public class QuotationService {

    private final QuotationRequestRepository quotationRequestRepository;
    private final ProductRepository productRepository;
    private final EmailNotificationService emailNotificationService;

    public QuotationService(QuotationRequestRepository quotationRequestRepository, ProductRepository productRepository, EmailNotificationService emailNotificationService) {
        this.quotationRequestRepository = quotationRequestRepository;
        this.productRepository = productRepository;
        this.emailNotificationService = emailNotificationService;
    }

    public QuotationRequest createQuotation(CustomerProfile customer, QuotationRequestCreate request) {
        QuotationRequest quotationRequest = new QuotationRequest();
        quotationRequest.setRequestReference("QTN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        quotationRequest.setCustomer(customer);
        quotationRequest.setDescription(request.getDescription());
        quotationRequest.setCreatedAt(Instant.now());
        quotationRequest.setStatus(QuotationStatus.PENDING);

        HashSet<QuotationItem> items = request.getItems().stream().map(itemRequest -> {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + itemRequest.getProductId()));
            QuotationItem item = new QuotationItem();
            item.setQuotationRequest(quotationRequest);
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            return item;
        }).collect(Collectors.toCollection(HashSet::new));

        quotationRequest.setItems(items);
        return quotationRequestRepository.save(quotationRequest);
    }

    public List<QuotationRequest> getCustomerQuotations(CustomerProfile customer) {
        return quotationRequestRepository.findByCustomer(customer);
    }

    public List<QuotationRequest> getVendorQuotations(VendorProfile vendor) {
        return quotationRequestRepository.findByVendor(vendor);
    }

    public List<QuotationRequest> getPendingQuotations() {
        return quotationRequestRepository.findByStatus(QuotationStatus.PENDING);
    }

    public Optional<QuotationRequest> assignVendor(Long quotationId, VendorProfile vendor) {
        return quotationRequestRepository.findById(quotationId).map(quotation -> {
            quotation.setVendor(vendor);
            quotation.setStatus(QuotationStatus.ASSIGNED);
            QuotationRequest saved = quotationRequestRepository.save(quotation);
            emailNotificationService.notifyQuotationAssigned(saved);
            return saved;
        });
    }
}

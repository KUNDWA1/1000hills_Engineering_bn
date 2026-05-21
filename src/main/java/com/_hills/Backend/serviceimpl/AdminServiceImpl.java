package com._hills.Backend.serviceimpl;

import com._hills.Backend.dto.AdminCreateRequest;
import com._hills.Backend.dto.AdminDashboardResponse;
import com._hills.Backend.dto.SalesReportResponse;
import com._hills.Backend.entity.*;
import com._hills.Backend.exception.ResourceNotFoundException;
import com._hills.Backend.repository.*;
import com._hills.Backend.service.AdminService;
import com._hills.Backend.util.AppConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageImpl;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final VendorRepository vendorRepository;
    private final VendorApprovalRepository vendorApprovalRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductApprovalRepository productApprovalRepository;
    private final com._hills.Backend.repository.OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public AdminServiceImpl(AdminRepository adminRepository,
                            VendorRepository vendorRepository,
                            VendorApprovalRepository vendorApprovalRepository,
                            UserRepository userRepository, ProductRepository productRepository,
                            ProductApprovalRepository productApprovalRepository,
                            com._hills.Backend.repository.OrderRepository orderRepository,
                            PaymentRepository paymentRepository) {
        this.adminRepository = adminRepository;
        this.vendorRepository = vendorRepository;
        this.vendorApprovalRepository = vendorApprovalRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productApprovalRepository = productApprovalRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public Admin authenticate(String username, String password) {
        Optional<Admin> adminOpt = adminRepository.findByUsernameOrEmail(username, username)
                .filter(a -> a.getPassword().equals(password))
                .filter(Admin::isActive);
        if (adminOpt.isPresent()) return adminOpt.get();
        throw new ResourceNotFoundException(AppConstants.INVALID_CREDENTIALS);
    }

    @Override
    @Transactional
    public Admin createAdmin(AdminCreateRequest request) {
        if (adminRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already taken");
        }
        if (adminRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already in use");
        }
        Admin admin = new Admin(request.fullName(), request.username(), request.email(), request.password());
        if (request.phoneNumber() != null)
            admin.setPhoneNumber(request.phoneNumber());
        return adminRepository.save(admin);
    }

    @Override
    public Admin getAdminProfile(Long adminId) {
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
    }

    @Override
    @Transactional
    public Admin updateAdminProfile(Long adminId, AdminCreateRequest request) {
        Admin admin = getAdminProfile(adminId);
        admin.setFullName(request.fullName());
        admin.setEmail(request.email());
        if (request.password() != null && !request.password().isBlank())
            admin.setPassword(request.password());
        if (request.phoneNumber() != null)
            admin.setPhoneNumber(request.phoneNumber());
        return adminRepository.save(admin);
    }

    @Override
    public AdminDashboardResponse getDashboardStats() {
        long totalVendors = vendorRepository.count();
        long activeVendors = vendorRepository.countByActive(true);
        long totalCustomers = userRepository.count();
        long totalProducts = productRepository.count();
        long pendingProducts = productRepository.countByStatus(AppConstants.STATUS_PENDING);
        long totalOrders = orderRepository.count();
        long pendingOrders = orderRepository.countByStatus(AppConstants.STATUS_PENDING);
        long approvedOrders = orderRepository.countByStatus(AppConstants.STATUS_APPROVED);
        long deliveredOrders = orderRepository.countByStatus(AppConstants.STATUS_DELIVERED);
        long cancelledOrders = orderRepository.countByStatus(AppConstants.STATUS_CANCELLED);
        long pendingPayments = paymentRepository.countByStatus(AppConstants.STATUS_PENDING);

        BigDecimal totalRevenue = paymentRepository.findByStatus(AppConstants.STATUS_APPROVED, Pageable.ofSize(Integer.MAX_VALUE))
                .getContent().stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new AdminDashboardResponse(
                totalVendors, activeVendors, totalCustomers,
                totalOrders, pendingOrders, approvedOrders,
                deliveredOrders, cancelledOrders, totalProducts, pendingProducts,
                totalRevenue, pendingPayments, 0, 0, 0,
                LocalDateTime.now()
        );
    }

    @Override
    public SalesReportResponse getSalesReport() {
        List<Payment> approvedPayments = paymentRepository.findByStatus(AppConstants.STATUS_APPROVED, Pageable.ofSize(10000)).getContent();
        BigDecimal totalRevenue = approvedPayments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Payment> pendingPaymentsList = paymentRepository.findByStatus(AppConstants.STATUS_PENDING, Pageable.ofSize(10000)).getContent();
        BigDecimal pendingAmount = pendingPaymentsList.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new SalesReportResponse(
                totalRevenue,
                orderRepository.count(),
                productRepository.count(),
                vendorRepository.count(),
                userRepository.count(),
                pendingAmount,
                totalRevenue
        );
    }

    @Override
    public Page<Vendor> getAllVendors(Pageable pageable) {
        return vendorRepository.findAll(pageable);
    }

    @Override
    public Page<Vendor> getVendorsByStatus(String status, Pageable pageable) {
        return vendorRepository.findByStatus(status.toUpperCase(), pageable);
    }

    @Override
    public long getTotalVendors() {
        return vendorRepository.count();
    }

    @Override
    public long getPendingVendorCount() {
        return vendorRepository.countByStatus(AppConstants.STATUS_PENDING);
    }

    @Override
    @Transactional
    public VendorApproval approveVendor(Long vendorId, String adminRemarks, Long adminId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        vendor.setStatus(AppConstants.STATUS_APPROVED);
        vendor.setActive(true);
        vendorRepository.save(vendor);

        VendorApproval approval = new VendorApproval(vendor);
        approval.setStatus(AppConstants.STATUS_APPROVED);
        approval.setAdminRemarks(adminRemarks);
        return vendorApprovalRepository.save(approval);
    }

    @Override
    @Transactional
    public VendorApproval rejectVendor(Long vendorId, String adminRemarks, Long adminId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        vendor.setStatus(AppConstants.STATUS_REJECTED);
        vendor.setActive(false);
        vendorRepository.save(vendor);

        VendorApproval approval = vendorApprovalRepository.findByVendorId(vendorId);
        if (approval == null) approval = new VendorApproval(vendor);
        approval.setStatus(AppConstants.STATUS_REJECTED);
        approval.setAdminRemarks(adminRemarks);
        return vendorApprovalRepository.save(approval);
    }

    @Override
    @Transactional
    public Vendor updateVendorStatus(Long vendorId, String status) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + vendorId));
        vendor.setStatus(status);
        vendor.setActive(AppConstants.STATUS_APPROVED.equalsIgnoreCase(status));
        return vendorRepository.save(vendor);
    }

    @Override
    @Transactional
    public Product approveProduct(Long productId, Long adminId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setStatus(AppConstants.STATUS_APPROVED);
        productRepository.save(product);

        ProductApproval approval = new ProductApproval();
        approval.setProduct(product);
        approval.setStatus(AppConstants.STATUS_APPROVED);
        productApprovalRepository.save(approval);
        return product;
    }

    @Override
    @Transactional
    public Product rejectProduct(Long productId, String adminRemarks, Long adminId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setStatus(AppConstants.STATUS_REJECTED);
        product.setAvailable(false);
        productRepository.save(product);

        ProductApproval approval = productApprovalRepository.findByProductId(productId);
        if (approval == null) approval = new ProductApproval();
        approval.setProduct(product);
        approval.setStatus(AppConstants.STATUS_REJECTED);
        approval.setAdminRemarks(adminRemarks);
        productApprovalRepository.save(approval);
        return product;
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> getPendingProducts(Pageable pageable) {
        return productRepository.findByStatus(AppConstants.STATUS_PENDING, pageable);
    }

    @Override
    @Transactional
    public Product updateProductAvailability(Long productId, boolean available) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setAvailable(available);
        return productRepository.save(product);
    }

    @Override
    public Page<User> getAllCustomers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public long getTotalCustomers() {
        return userRepository.count();
    }

    public List<Vendor> findAvailableVendorsForProduct(Long productId) {
        return vendorRepository.findAll().stream()
                .filter(Vendor::isActive)
                .filter(v -> AppConstants.STATUS_APPROVED.equalsIgnoreCase(v.getStatus()))
                .filter(v -> AppConstants.VENDOR_AVAILABLE.equalsIgnoreCase(v.getAvailability()))
                .sorted((v1, v2) -> {
                    int cmp = Boolean.compare(
                            AppConstants.VENDOR_AVAILABLE.equalsIgnoreCase(v2.getAvailability()),
                            AppConstants.VENDOR_AVAILABLE.equalsIgnoreCase(v1.getAvailability()));
                    if (cmp != 0) return cmp;
                    return v1.getCompanyName().compareToIgnoreCase(v2.getCompanyName());
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<Product> getLowStockProducts(Pageable pageable) {
        org.springframework.data.domain.Page<Product> pageResult = productRepository.findByStatusAndAvailable(AppConstants.STATUS_APPROVED, true, Pageable.ofSize(Integer.MAX_VALUE));
        java.util.List<Product> allProducts = pageResult.getContent();
        java.util.List<Product> lowStock = allProducts.stream()
                .filter(p -> p.getStockQuantity() <= AppConstants.LOW_STOCK_THRESHOLD)
                .collect(java.util.stream.Collectors.toList());
        return new PageImpl<>(lowStock, pageable, lowStock.size());
    }
}

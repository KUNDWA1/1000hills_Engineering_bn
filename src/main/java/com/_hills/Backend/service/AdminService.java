package com._hills.Backend.service;

import com._hills.Backend.dto.AdminCreateRequest;
import com._hills.Backend.dto.AdminDashboardResponse;
import com._hills.Backend.dto.SalesReportResponse;
import com._hills.Backend.entity.Admin;
import com._hills.Backend.entity.Product;
import com._hills.Backend.entity.User;
import com._hills.Backend.entity.Vendor;
import com._hills.Backend.entity.VendorApproval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface AdminService {

    Admin authenticate(String username, String password);

    Admin createAdmin(AdminCreateRequest request);

    Admin getAdminProfile(Long adminId);

    Admin updateAdminProfile(Long adminId, AdminCreateRequest request);

    AdminDashboardResponse getDashboardStats();

    SalesReportResponse getSalesReport();

    Page<Vendor> getAllVendors(Pageable pageable);

    Page<Vendor> getVendorsByStatus(String status, Pageable pageable);

    long getTotalVendors();

    long getPendingVendorCount();

    VendorApproval approveVendor(Long vendorId, String adminRemarks, Long adminId);

    VendorApproval rejectVendor(Long vendorId, String adminRemarks, Long adminId);

    Vendor updateVendorStatus(Long vendorId, String status);

    Product approveProduct(Long productId, Long adminId);

    Product rejectProduct(Long productId, String adminRemarks, Long adminId);

    Page<Product> getAllProducts(Pageable pageable);

    Page<Product> getPendingProducts(Pageable pageable);

    Product updateProductAvailability(Long productId, boolean available);

    Page<User> getAllCustomers(Pageable pageable);

    long getTotalCustomers();

    Page<Product> getLowStockProducts(Pageable pageable);
}

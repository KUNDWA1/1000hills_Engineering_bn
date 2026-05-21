package com._hills.Backend.service;

import com._hills.Backend.dto.AdminResponse;
import com._hills.Backend.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface VendorManager {

    List<Vendor> findAvailableVendorsForProduct(Long productId);

    Order assignOrderToVendor(Long orderId, Long vendorId, Long adminId, String adminRemarks);

    Order updateOrderStatus(Long orderId, String status);

    Order assignAdminToQuotation(Long quotationId, Long vendorId, Long adminId);

    Quotation assignVendorToQuotation(Long quotationId, Long vendorId, Long adminId);

    List<Vendor> getBestVendorsForProduct(Long productId);

    Delivery createDelivery(Long orderId, String deliveryAddress, String courierName);

    Delivery updateDeliveryStatus(Long deliveryId, String status);
}

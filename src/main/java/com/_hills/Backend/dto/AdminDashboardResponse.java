package com._hills.Backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AdminDashboardResponse(
        long totalVendors,
        long totalActiveVendors,
        long totalCustomers,
        long totalOrders,
        long pendingOrders,
        long approvedOrders,
        long deliveredOrders,
        long cancelledOrders,
        long totalProducts,
        long pendingProducts,
        BigDecimal totalRevenue,
        long pendingPayments,
        long approvedPayments,
        long approvedQuotations,
        long pendingQuotations,
        LocalDateTime lastUpdated
) {}

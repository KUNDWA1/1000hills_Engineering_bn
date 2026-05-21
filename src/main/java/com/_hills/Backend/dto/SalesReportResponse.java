package com._hills.Backend.dto;

import java.math.BigDecimal;

public record SalesReportResponse(
        BigDecimal totalRevenue,
        long totalOrders,
        long totalProducts,
        long totalVendors,
        long totalCustomers,
        BigDecimal pendingAmount,
        BigDecimal approvedAmount
) {}

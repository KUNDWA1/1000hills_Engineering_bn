package com.thousandhills.backend.model;

public enum OrderStatus {
    PENDING,
    APPROVED,
    ASSIGNED_TO_VENDOR,
    PROCESSING,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED
}

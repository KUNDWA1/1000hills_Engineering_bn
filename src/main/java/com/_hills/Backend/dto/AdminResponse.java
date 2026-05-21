package com._hills.Backend.dto;

import com._hills.Backend.entity.Admin;
import java.time.LocalDateTime;

public record AdminResponse(
        Long id,
        String fullName,
        String username,
        String email,
        String phoneNumber,
        String role,
        boolean active,
        LocalDateTime createdAt
) {
    public static AdminResponse from(Admin admin) {
        return new AdminResponse(
                admin.getId(),
                admin.getFullName(),
                admin.getUsername(),
                admin.getEmail(),
                admin.getPhoneNumber(),
                admin.getRole(),
                admin.isActive(),
                admin.getCreatedAt()
        );
    }
}

package com._hills.Backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public record AdminCreateRequest(
        String fullName,
        String username,
        @Email String email,
        String password,
        String phoneNumber
) {}

package com._hills.Backend.controller;

import com._hills.Backend.dto.AdminCreateRequest;
import com._hills.Backend.dto.AdminLoginRequest;
import com._hills.Backend.dto.AdminResponse;
import com._hills.Backend.entity.Admin;
import com._hills.Backend.service.AdminService;
import com._hills.Backend.service.AuthService;
import com._hills.Backend.util.AppConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final AdminService adminService;

    public AuthController(AuthService authService, AdminService adminService) {
        this.authService = authService;
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminLoginRequest request) {
        return authService.validateAdmin(request.username(), request.password())
                .<ResponseEntity<?>>map(admin -> ResponseEntity.ok(new java.util.HashMap<>() {{
                    put("success", true);
                    put("message", "Login successful");
                    put("data", AdminResponse.from(admin));
                }}))
                .orElseGet(() -> ResponseEntity.status(401).body(java.util.Map.of(
                        "success", false,
                        "message", AppConstants.INVALID_CREDENTIALS
                )));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AdminCreateRequest request) {
        try {
            Admin created = adminService.createAdmin(request);
            return ResponseEntity.ok(java.util.Map.of(
                    "success", true,
                    "message", "Admin created successfully",
                    "data", AdminResponse.from(created)
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/admins")
    public ResponseEntity<?> getAllAdmins() {
        List<AdminResponse> admins = authService.getAllAdmins();
        return ResponseEntity.ok(java.util.Map.of(
                "success", true,
                "data", admins
        ));
    }
}

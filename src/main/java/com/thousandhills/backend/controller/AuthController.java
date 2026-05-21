package com.thousandhills.backend.controller;

import java.security.Principal;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.thousandhills.backend.dto.RegisterRequest;
import com.thousandhills.backend.model.AppUser;
import com.thousandhills.backend.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/customer")
    public ResponseEntity<?> registerCustomer(@RequestBody RegisterRequest request) {
        return authService.registerCustomer(request)
                .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(Map.of("username", user.getUsername(), "role", user.getRole())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Username or email already exists")));
    }

    @PostMapping("/register/vendor")
    public ResponseEntity<?> registerVendor(@RequestBody RegisterRequest request) {
        return authService.registerVendor(request)
                .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(Map.of("username", user.getUsername(), "role", user.getRole())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Username or email already exists")));
    }

    @GetMapping("/me")
    public ResponseEntity<?> currentUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Not authenticated"));
        }
        return ResponseEntity.ok(Map.of("username", principal.getName()));
    }
}

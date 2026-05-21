package com.thousandhills.backend.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.thousandhills.backend.dto.RegisterRequest;
import com.thousandhills.backend.model.AppUser;
import com.thousandhills.backend.model.UserRole;
import com.thousandhills.backend.service.AuthService;
import com.thousandhills.backend.service.JwtService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, JwtService jwtService,
                          AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        AppUser user = (AppUser) auth.getPrincipal();
        String role = user.getRole().name().replace("ROLE_", "");
        String token = jwtService.generateToken(user.getUsername(), role);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "type", "Bearer",
                "username", user.getUsername(),
                "role", role
        ));
    }

    @PostMapping("/register/customer")
    public ResponseEntity<?> registerCustomer(@RequestBody RegisterRequest request) {
        return authService.registerCustomer(request)
                .map(user -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of("username", user.getUsername(), "role", user.getRole())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Username or email already exists")));
    }

    @PostMapping("/register/vendor")
    public ResponseEntity<?> registerVendor(@RequestBody RegisterRequest request) {
        return authService.registerVendor(request)
                .map(user -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of("username", user.getUsername(), "role", user.getRole())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Username or email already exists")));
    }

    @GetMapping("/me")
    public ResponseEntity<?> currentUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }
        return ResponseEntity.ok(Map.of("username", principal.getName(), "roles", List.of("ALL")));
    }
}

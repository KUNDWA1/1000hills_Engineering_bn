package com.thousandhills.backend.service;

import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.thousandhills.backend.dto.RegisterRequest;
import com.thousandhills.backend.model.AppUser;
import com.thousandhills.backend.model.CustomerProfile;
import com.thousandhills.backend.model.UserRole;
import com.thousandhills.backend.model.VendorProfile;
import com.thousandhills.backend.repository.CustomerProfileRepository;
import com.thousandhills.backend.repository.UserRepository;
import com.thousandhills.backend.repository.VendorProfileRepository;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final VendorProfileRepository vendorProfileRepository;
    private final CustomerProfileRepository customerProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       VendorProfileRepository vendorProfileRepository,
                       CustomerProfileRepository customerProfileRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.vendorProfileRepository = vendorProfileRepository;
        this.customerProfileRepository = customerProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<AppUser> registerCustomer(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername()) || userRepository.existsByEmail(request.getEmail())) {
            return Optional.empty();
        }

        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.CUSTOMER);
        userRepository.save(user);

        CustomerProfile customerProfile = new CustomerProfile();
        customerProfile.setFullName(request.getFullName());
        customerProfile.setPhoneNumber(request.getPhoneNumber());
        customerProfile.setCompanyName(request.getCompanyName());
        customerProfile.setAddress(request.getAddress());
        customerProfile.setUser(user);
        customerProfileRepository.save(customerProfile);

        return Optional.of(user);
    }

    public Optional<AppUser> registerVendor(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername()) || userRepository.existsByEmail(request.getEmail())) {
            return Optional.empty();
        }

        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.VENDOR);
        userRepository.save(user);

        VendorProfile vendorProfile = new VendorProfile();
        vendorProfile.setCompanyName(request.getCompanyName());
        vendorProfile.setCompanyAddress(request.getAddress());
        vendorProfile.setUser(user);
        vendorProfileRepository.save(vendorProfile);

        return Optional.of(user);
    }
}

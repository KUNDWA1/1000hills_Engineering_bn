package com._hills.Backend.serviceimpl;

import com._hills.Backend.dto.AdminResponse;
import com._hills.Backend.entity.Admin;
import com._hills.Backend.exception.ResourceNotFoundException;
import com._hills.Backend.repository.AdminRepository;
import com._hills.Backend.service.AuthService;
import com._hills.Backend.util.AppConstants;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final AdminRepository adminRepository;

    public AuthServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public Optional<Admin> validateAdmin(String username, String password) {
        return adminRepository.findByUsernameOrEmail(username, username)
                .filter(a -> a.getPassword().equals(password))
                .filter(Admin::isActive);
    }

    @Override
    public List<AdminResponse> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(AdminResponse::from)
                .collect(Collectors.toList());
    }
}

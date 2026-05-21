package com._hills.Backend.service;

import com._hills.Backend.dto.AdminResponse;
import com._hills.Backend.entity.Admin;
import java.util.List;
import java.util.Optional;

public interface AuthService {
    Optional<Admin> validateAdmin(String username, String password);
    List<AdminResponse> getAllAdmins();
}

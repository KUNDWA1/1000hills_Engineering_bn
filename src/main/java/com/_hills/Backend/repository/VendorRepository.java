package com._hills.Backend.repository;

import com._hills.Backend.entity.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Optional<Vendor> findByUsername(String username);
    Optional<Vendor> findByEmail(String email);
    Page<Vendor> findAll(Pageable pageable);
    Page<Vendor> findByStatus(String status, Pageable pageable);
    long countByActive(boolean active);
    long countByStatus(String status);
}

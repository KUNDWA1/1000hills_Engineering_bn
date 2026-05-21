package com._hills.Backend.repository;

import com._hills.Backend.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByAssignedVendorId(Long vendorId, Pageable pageable);
    Page<Order> findByStatus(String status, Pageable pageable);
    List<Order> findByCustomerId(Long userId);
    long countByStatus(String status);
    long countByAssignedVendorIdAndStatus(Long vendorId, String status);
}

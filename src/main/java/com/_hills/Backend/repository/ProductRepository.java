package com._hills.Backend.repository;

import com._hills.Backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByVendorId(Long vendorId, Pageable pageable);
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Product> findByStatus(String status, Pageable pageable);
    Page<Product> findByStatusAndVendorId(String status, Long vendorId, Pageable pageable);
    Product findByIdAndStatus(Long productId, String status);
    Page<Product> findByStatusAndAvailable(String status, boolean available, Pageable pageable);
    long countByStatus(String status);
    long countByVendorIdAndStatus(Long vendorId, String status);
    long count();
}

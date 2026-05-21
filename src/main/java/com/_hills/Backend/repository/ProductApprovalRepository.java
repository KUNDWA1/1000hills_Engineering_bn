package com._hills.Backend.repository;

import com._hills.Backend.entity.ProductApproval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductApprovalRepository extends JpaRepository<ProductApproval, Long> {
    ProductApproval findByProductId(Long productId);
    Page<ProductApproval> findByStatus(String status, Pageable pageable);
}

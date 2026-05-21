package com._hills.Backend.repository;

import com._hills.Backend.entity.VendorApproval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorApprovalRepository extends JpaRepository<VendorApproval, Long> {
    Page<VendorApproval> findByStatus(String status, Pageable pageable);
    VendorApproval findByVendorId(Long vendorId);
}

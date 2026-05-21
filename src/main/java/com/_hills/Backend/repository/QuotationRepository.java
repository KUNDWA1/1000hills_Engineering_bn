package com._hills.Backend.repository;

import com._hills.Backend.entity.Quotation;
import com._hills.Backend.entity.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Long> {
    Page<Quotation> findByAssignedVendorId(Long vendorId, Pageable pageable);
    Page<Quotation> findByCustomerId(Long customerId, Pageable pageable);
}

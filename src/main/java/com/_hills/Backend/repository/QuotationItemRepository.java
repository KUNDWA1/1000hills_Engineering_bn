package com._hills.Backend.repository;

import com._hills.Backend.entity.Quotation;
import com._hills.Backend.entity.QuotationItem;
import com._hills.Backend.entity.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationItemRepository extends JpaRepository<QuotationItem, Long> {
    Page<QuotationItem> findByQuotationId(Long quotationId, org.springframework.data.domain.Pageable pageable);
}

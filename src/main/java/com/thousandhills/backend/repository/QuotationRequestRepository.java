package com.thousandhills.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.thousandhills.backend.model.CustomerProfile;
import com.thousandhills.backend.model.QuotationRequest;
import com.thousandhills.backend.model.QuotationStatus;
import com.thousandhills.backend.model.VendorProfile;

@Repository
public interface QuotationRequestRepository extends JpaRepository<QuotationRequest, Long> {

    List<QuotationRequest> findByCustomer(CustomerProfile customer);

    List<QuotationRequest> findByVendor(VendorProfile vendor);

    List<QuotationRequest> findByStatus(QuotationStatus status);
}

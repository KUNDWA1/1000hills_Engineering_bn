package com.thousandhills.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.thousandhills.backend.model.VendorProfile;
import com.thousandhills.backend.model.VendorStatus;

@Repository
public interface VendorProfileRepository extends JpaRepository<VendorProfile, Long> {

    Optional<VendorProfile> findByUserId(Long userId);

    List<VendorProfile> findByStatus(VendorStatus status);
}

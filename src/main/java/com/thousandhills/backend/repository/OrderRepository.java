package com.thousandhills.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.thousandhills.backend.model.Order;
import com.thousandhills.backend.model.CustomerProfile;
import com.thousandhills.backend.model.VendorProfile;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomer(CustomerProfile customer);

    List<Order> findByVendor(VendorProfile vendor);

    // NEW: Find orders without vendor assigned (pending vendor assignment)
    @Query("SELECT o FROM Order o WHERE o.vendor IS NULL")
    List<Order> findByVendorIsNull();
}

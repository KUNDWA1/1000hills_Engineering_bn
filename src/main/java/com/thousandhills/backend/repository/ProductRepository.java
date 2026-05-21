package com.thousandhills.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.thousandhills.backend.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Custom query to find featured items for the homepage landing page
    List<Product> findByIsFeaturedTrueAndIsApprovedTrue();

    // Public catalog should only expose approved items
    List<Product> findByIsApprovedTrue();

    // Get approved products for a specific vendor
    List<Product> findAllByVendorIdAndIsApprovedTrue(Long vendorId);

    Optional<Product> findByIdAndIsApprovedTrue(Long id);

    List<Product> findByCategoryAndIdNotAndIsApprovedTrue(String category, Long id);

    // 2-arg convenience for ProductService:
    default List<Product> findByCategoryAndIdNot(String category, Long id) {
        return findByCategoryAndIdNotAndIsApprovedTrue(category, id);
    }

    List<Product> findByIsApprovedFalse();

    // NEW: Find approved products by SKU (for vendor matching)
    List<Product> findBySkuAndIsApprovedTrue(String sku);
    
    // NEW: Find vendors who have a specific product (by ID) and are approved
    @Query("SELECT p.vendor.id, p.vendor.companyName, p.vendor.availabilityStatus, p.vendor.reliabilityRating, p.stockQuantity " +
           "FROM Product p " +
           "WHERE p.id = :productId AND p.isApproved = true AND p.vendor.status = 'APPROVED'")
    List<Object[]> findAvailableVendorsForProduct(Long productId);
}
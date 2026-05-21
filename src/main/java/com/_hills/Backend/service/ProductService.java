package com._hills.Backend.service;

import com._hills.Backend.entity.Product;
import com._hills.Backend.entity.Vendor;
import com._hills.Backend.entity.Order;
import com._hills.Backend.entity.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductService {

    Product addProduct(Product product);

    Product updateStock(Long productId, Integer newStock);

    Page<Product> filterProducts(Long vendorId, Long categoryId, String keyword, Pageable pageable);

    Map<Long, List<Product>> groupProductsByVendor(Long productId);
}

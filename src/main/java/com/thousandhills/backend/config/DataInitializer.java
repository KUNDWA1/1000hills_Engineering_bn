package com.thousandhills.backend.config;

import com.thousandhills.backend.model.AppUser;
import com.thousandhills.backend.model.Product;
import com.thousandhills.backend.model.ProductImage;
import com.thousandhills.backend.model.ProductSpecification;
import com.thousandhills.backend.model.UserRole;
import com.thousandhills.backend.repository.ProductRepository;
import com.thousandhills.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor Injection
    public DataInitializer(ProductRepository productRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Prevent duplicating data if the server restarts
        if (productRepository.count() == 0) {
            System.out.println(">>> Database is empty. Seeding professional e-commerce sample data...");

            // ==========================================
            // PRODUCT 1: Premium Solar Inverter (Featured)
            // ==========================================
            Product inverter = new Product();
            inverter.setSku("INV-5KW-001");
            inverter.setTitle("Smart Hybrid Solar Inverter 5kW");
            inverter.setPrice(1200.00);
            inverter.setCurrency("USD");
            inverter.setCategory("Inverters");
            inverter.setImageUrl("https://images.unsplash.com/photo-1620714223084-8fcacc6dfd8d?w=500");
            inverter.setStockQuantity(15);
            inverter.setIsFeatured(true);
            inverter.setDescription("High-efficiency hybrid solar inverter supporting simultaneous solar input, grid backup, and battery management.");

            // Relational Child Data for Product 1
            ProductImage img1 = new ProductImage(null, "https://images.unsplash.com/photo-1620714223084-8fcacc6dfd8d?w=500", inverter);
            ProductSpecification spec1 = new ProductSpecification(null, "Output Power", "5000W", inverter);
            ProductSpecification spec2 = new ProductSpecification(null, "Efficiency", "98.4%", inverter);

            inverter.setImages(Set.of(img1));
            inverter.setSpecifications(Set.of(spec1, spec2));

            // ==========================================
            // PRODUCT 2: Lithium Battery Bank (Featured)
            // ==========================================
            Product battery = new Product();
            battery.setSku("BAT-LITH-100");
            battery.setTitle("Lithium LiFePO4 Battery Pack 48V 100Ah");
            battery.setPrice(1850.00);
            battery.setCurrency("USD");
            battery.setCategory("Batteries");
            battery.setImageUrl("https://images.unsplash.com/photo-1548345680-f5475ea5df84?w=500");
            battery.setStockQuantity(8);
            battery.setIsFeatured(true);
            battery.setDescription("Deep-cycle lithium iron phosphate battery with built-in advanced Smart BMS protection system.");

            ProductImage img2 = new ProductImage(null, "https://images.unsplash.com/photo-1548345680-f5475ea5df84?w=500", battery);
            ProductSpecification spec3 = new ProductSpecification(null, "Voltage", "51.2V", battery);
            ProductSpecification spec4 = new ProductSpecification(null, "Capacity", "100Ah", battery);

            battery.setImages(Set.of(img2));
            battery.setSpecifications(Set.of(spec3, spec4));

            // ==========================================
            // PRODUCT 3: Monocrystalline Solar Panel (Standard)
            // ==========================================
            Product panel = new Product();
            panel.setSku("PNL-MONO-450");
            panel.setTitle("450W Monocrystalline Solar Panel");
            panel.setPrice(150.00);
            panel.setCurrency("USD");
            panel.setCategory("Panels");
            panel.setImageUrl("https://images.unsplash.com/photo-1509391366360-2e959784a276?w=500");
            panel.setStockQuantity(50);
            panel.setIsFeatured(false); // Testing regular sorting
            panel.setDescription("Tier-1 high-density monocrystalline panels optimized for low-light environments and extreme climates.");

            ProductImage img3 = new ProductImage(null, "https://images.unsplash.com/photo-1509391366360-2e959784a276?w=500", panel);
            ProductSpecification spec5 = new ProductSpecification(null, "Max Power", "450W", panel);

            panel.setImages(Set.of(img3));
            panel.setSpecifications(Set.of(spec5));

            // Save parent objects (CascadeType.ALL takes care of saving the images and specs automatically!)
            productRepository.saveAll(Set.of(inverter, battery, panel));
            System.out.println(">>> Sample data successfully written to MySQL database catalog storage.");
        } else {
            System.out.println(">>> Database already contains product records. Skipping initialization routine.");
        }

        if (userRepository.count() == 0) {
            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setEmail("admin@1000hills.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setRole(UserRole.ADMIN);
            userRepository.save(admin);
            System.out.println(">>> Admin account created: admin / Admin@123");
        } else {
            System.out.println(">>> User records already exist. Skipping admin seeding.");
        }
    }
}
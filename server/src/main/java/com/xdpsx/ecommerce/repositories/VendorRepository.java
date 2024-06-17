package com.xdpsx.ecommerce.repositories;

import com.xdpsx.ecommerce.entities.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {
    boolean existsByName(String name);
}

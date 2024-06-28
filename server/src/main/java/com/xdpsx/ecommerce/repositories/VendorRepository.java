package com.xdpsx.ecommerce.repositories;

import com.xdpsx.ecommerce.entities.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VendorRepository extends JpaRepository<Vendor, Integer>, JpaSpecificationExecutor<Vendor> {
    boolean existsByName(String name);
}

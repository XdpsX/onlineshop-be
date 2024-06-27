package com.xdpsx.ecommerce.repositories;

import com.xdpsx.ecommerce.entities.Vendor;
import com.xdpsx.ecommerce.repositories.criteria.VendorCriteriaRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Integer>, VendorCriteriaRepository {
    boolean existsByName(String name);
}

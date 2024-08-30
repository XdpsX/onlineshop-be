package com.xdpsx.onlineshop.repositories;

import com.xdpsx.onlineshop.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    boolean existsByName(String name);

    @Query(value =
            "SELECT COUNT(*) FROM (" +
                "SELECT brand_id FROM category_brands WHERE brand_id = ?1 " +
            ") AS combined"
            , nativeQuery = true)
    long countBrandsInOtherTables(Integer brandId);

}

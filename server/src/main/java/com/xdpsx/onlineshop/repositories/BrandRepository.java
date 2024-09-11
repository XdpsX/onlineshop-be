package com.xdpsx.onlineshop.repositories;

import com.xdpsx.onlineshop.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Integer>, JpaSpecificationExecutor<Brand> {
    boolean existsByName(String name);

    @Query("SELECT b FROM Brand b JOIN b.categories c WHERE c.id = :categoryId ORDER BY b.name")
    List<Brand> findBrandsByCategoryId(Integer categoryId);

//    @Query(value =
//            "SELECT COUNT(*) FROM (" +
//                "SELECT brand_id FROM category_brands WHERE brand_id = ?1 " +
//            ") AS combined"
//            , nativeQuery = true)
//    long countBrandsInOtherTables(Integer brandId);

}

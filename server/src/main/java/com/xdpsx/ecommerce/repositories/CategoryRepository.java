package com.xdpsx.ecommerce.repositories;

import com.xdpsx.ecommerce.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryRepository extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {
    boolean existsByName(String name);
}

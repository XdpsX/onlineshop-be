package com.xdpsx.onlineshop.repositories;

import com.xdpsx.onlineshop.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);
}

package com.xdpsx.onlineshop.repositories;

import com.xdpsx.onlineshop.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    boolean existsBySlug(String slug);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.images WHERE p.id = :id")
    Optional<Product> findProductById(Long id);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.images WHERE p.slug = :slug")
    Optional<Product> findProductBySlug(String slug);
}

package com.xdpsx.onlineshop.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xdpsx.onlineshop.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {
    boolean existsByName(String name);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.parent WHERE c.id = :id")
    Optional<Category> findByIdWithParent(@Param("id") Integer id);

    @Query(
            value =
                    """
		SELECT COUNT(*) FROM (
			SELECT category_id FROM category_brands WHERE category_id = ?1
			UNION ALL
			SELECT category_id FROM products WHERE category_id = ?1
		) AS combined
	""",
            nativeQuery = true)
    long countCategoriesInOtherTables(Integer categoryId);
}

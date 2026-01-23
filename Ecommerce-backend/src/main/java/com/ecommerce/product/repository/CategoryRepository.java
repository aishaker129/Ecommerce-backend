package com.ecommerce.product.repository;

import com.ecommerce.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCode(String s);

    boolean existsByCode( String code);

    Optional<Category> findByNameIgnoreCase(String category);

    Optional<Category> findCategoryById(Long id);
}

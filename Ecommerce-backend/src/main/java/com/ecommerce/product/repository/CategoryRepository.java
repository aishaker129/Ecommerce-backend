package com.ecommerce.product.repository;

import com.ecommerce.product.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCode(String s);

    boolean existsByCode( String code);

    boolean existsByNameIgnoreCase(String name);

    Optional<Category> findByNameIgnoreCase(String category);
}

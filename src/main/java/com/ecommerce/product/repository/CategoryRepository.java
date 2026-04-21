package com.ecommerce.product.repository;

import com.ecommerce.product.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    boolean existsByCode(@NotBlank(message = "Category code is required.") String code);

    Optional<Category> findByCode( String s);
}

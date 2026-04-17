package com.ecommerce.product.repository;

import com.ecommerce.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsBySku( String sku);

    Optional<Product> findAllByIdAndIsActive(Long id, boolean b);

    Page<Product> findByIsActive(boolean b,Pageable pageable);

    @Query("""
    SELECT p FROM Product p
    WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
    AND p.isActive = true
""")
    Page<Product> searchActiveProducts(@Param("keyword") String keyword, Pageable pageable);
}

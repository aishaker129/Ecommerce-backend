package com.ecommerce.product.services;

import com.ecommerce.product.dto.ProductCategoryUpdateRequest;
import com.ecommerce.product.dto.ProductCreateRequest;
import com.ecommerce.product.dto.ProductUpdateRequest;
import com.ecommerce.product.entity.Product;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Product createProduct( ProductCreateRequest request);

    Page<Product> findAllProduct(Pageable pageable);

    Product findProductById(Long id);

    Page<Product> filterProduct(String category, boolean status, Integer minPrice, Integer maxPrice, Pageable pageable);

    Product updateProduct( ProductUpdateRequest request);

    Product productCategoryUpdate(ProductCategoryUpdateRequest request,Long id);
}

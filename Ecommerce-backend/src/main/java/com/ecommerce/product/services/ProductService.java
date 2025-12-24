package com.ecommerce.product.services;

import com.ecommerce.product.dto.request.ProductCategoryUpdateRequest;
import com.ecommerce.product.dto.request.ProductCreateRequest;
import com.ecommerce.product.dto.request.ProductUpdateRequest;
import com.ecommerce.product.dto.response.ProductResponse;
import com.ecommerce.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponse createProduct(ProductCreateRequest request);

    Page<ProductResponse> findAllProduct(Pageable pageable);

    ProductResponse findProductById(Long id);

    Page<ProductResponse> filterProduct(String category, boolean status, Integer minPrice, Integer maxPrice, Pageable pageable);

    ProductResponse updateProduct( ProductUpdateRequest request);

    ProductResponse productCategoryUpdate(ProductCategoryUpdateRequest request,Long id);

    String toggleStatus(Long id, boolean b);
}

package com.ecommerce.product.services;

import com.ecommerce.product.dto.request.CategoryUpdateRequest;
import com.ecommerce.product.dto.request.CreateCategoryRequest;
import com.ecommerce.product.dto.response.CategoryResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CreateCategoryRequest request);

    CategoryResponse update(Long id, CategoryUpdateRequest request);

    List<CategoryResponse> fetchCategoryList(Pageable pageable);

    CategoryResponse toggleCategoryStatus(Long id, Boolean isActive);

    Void deleteCategory(Long id);
}

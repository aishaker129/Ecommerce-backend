package com.ecommerce.product.services;

import com.ecommerce.product.dto.request.CategoryCreateRequest;
import com.ecommerce.product.dto.request.CategoryUpdateRequest;
import com.ecommerce.product.entity.Category;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Category create(CategoryCreateRequest request);

    Category getCategoryById(Long id);

    Page<Category> getAll(Pageable pageable);

    Category updateCategory(Long id, @Valid CategoryUpdateRequest request);

    Category toggleCategoryStatus(Long id, Boolean isActive);

    String deleteCategory(Long id);
}

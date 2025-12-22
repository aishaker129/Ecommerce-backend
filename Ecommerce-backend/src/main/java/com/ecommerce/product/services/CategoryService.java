package com.ecommerce.product.services;

import com.ecommerce.product.dto.CategoryCreateRequest;
import com.ecommerce.product.entity.Category;
import jakarta.validation.Valid;

public interface CategoryService {
    Category create(CategoryCreateRequest request);
}

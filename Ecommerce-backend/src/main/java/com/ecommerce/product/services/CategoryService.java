package com.ecommerce.product.services;

import com.ecommerce.product.dto.request.CategoryCreateRequest;
import com.ecommerce.product.entity.Category;

public interface CategoryService {
    Category create(CategoryCreateRequest request);
}

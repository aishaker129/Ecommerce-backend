package com.ecommerce.product.services.impl;

import com.ecommerce.common.exceptions.ResourceConflictException;
import com.ecommerce.product.dto.request.CategoryCreateRequest;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.mapper.CategoryMapper;
import com.ecommerce.product.repository.CategoryRepository;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Category create(CategoryCreateRequest request) {
        if(categoryRepository.existsByCode(request.code())){
            throw new ResourceConflictException("Category with code '" + request.code() + "' already exists.");
        }
        if (categoryRepository.existsByNameIgnoreCase(request.name())){
            throw new ResourceConflictException("Category with name '" + request.name() + "' already exists.");
        }

        Category category = categoryMapper.toEntity(request);
        Category savecategory = categoryRepository.save(category);
        return savecategory;
    }
}

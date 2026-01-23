package com.ecommerce.product.services.impl;

import com.ecommerce.common.exceptions.ResourceConflictException;
import com.ecommerce.product.dto.request.CategoryCreateRequest;
import com.ecommerce.product.dto.request.CategoryUpdateRequest;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.mapper.CategoryMapper;
import com.ecommerce.product.repository.CategoryRepository;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link CategoryService} for managing product categories.
 *
 * <p>Provides CRUD operations and status management for categories
 * with unique code validation.</p>
 *
 * @author Md.Akhlakul Islam
 */

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
//        if (categoryRepository.existsByNameIgnoreCase(request.name())){
//            throw new ResourceConflictException("Category with name '" + request.name() + "' already exists.");
//        }

        Category category = categoryMapper.toEntity(request);
        Category savecategory = categoryRepository.save(category);
        return savecategory;
    }

    @Override
    public Category getCategoryById(Long id) {
        Category category = categoryRepository.findCategoryById(id).orElseThrow(()->
                new EntityNotFoundException("Category with id '"+id+"' not found"));
        return category;
    }

    @Override
    public Page<Category> getAll(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories;
    }

    @Override
    public Category updateCategory(Long id, CategoryUpdateRequest request) {
        Category category = categoryRepository.findCategoryById(id).orElseThrow(
                ()-> new EntityNotFoundException("Category with id '"+id+"' not found")
        );
        category.setName(request.name());
        return categoryRepository.save(category);
    }

    @Override
    public Category toggleCategoryStatus(Long id, Boolean isActive) {
        Category category = categoryRepository.findCategoryById(id).orElseThrow(
                ()-> new EntityNotFoundException("Category with id '"+id+"' not found")
        );

        category.setIsActive(isActive);
        return categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long id) {
        Category category = categoryRepository.findCategoryById(id).orElseThrow(
                ()-> new EntityNotFoundException("Category with id '"+id+"' not found")
        );

        categoryRepository.delete(category);
        return "Delete category successfully with id "+id;
    }


}

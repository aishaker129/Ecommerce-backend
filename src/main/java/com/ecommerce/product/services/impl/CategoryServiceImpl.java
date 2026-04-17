package com.ecommerce.product.services.impl;

import com.ecommerce.common.exceptions.IllegalOperationException;
import com.ecommerce.product.dto.request.CategoryUpdateRequest;
import com.ecommerce.product.dto.request.CreateCategoryRequest;
import com.ecommerce.product.dto.response.CategoryResponse;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.mapper.CategoryMapper;
import com.ecommerce.product.repository.CategoryRepository;
import com.ecommerce.product.services.CategoryService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        if(categoryRepository.existsByCode(request.code())){
            throw new EntityExistsException("Code already exists.");
        }
        Category category = categoryMapper.toEntity(request);
        category.onCreate();
        Category savedCategory = categoryRepository.save(category);
        CategoryResponse response = categoryMapper.toDto(savedCategory);
        return response;
    }

    @Override
    public CategoryResponse update(Long id, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Category not fouond.")
        );
        category.setName(request.name());
        CategoryResponse response = categoryMapper.toDto(categoryRepository.save(category));
        return response;
    }

    @Override
    public List<CategoryResponse> fetchCategoryList(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);

        List<CategoryResponse> responses = new ArrayList<>();
        for(var category: categories){
            CategoryResponse response = categoryMapper.toDto(category);
            responses.add(response);
        }
        return responses;
    }

    @Override
    public CategoryResponse toggleCategoryStatus(Long id, Boolean isActive) {
        Category  category= categoryRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Category not found with id: "+id+".")
        );

        category.setIsActive(isActive);
        CategoryResponse response = categoryMapper.toDto(categoryRepository.save(category));
        return response;
    }

    @Override
    public Void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Category not found.")
        );

        if(!category.getProducts().isEmpty()){
            throw new IllegalOperationException("Category contains products");
        }
        categoryRepository.delete(category);
        return null;
    }
}

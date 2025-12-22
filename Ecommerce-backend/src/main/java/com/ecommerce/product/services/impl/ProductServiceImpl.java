package com.ecommerce.product.services.impl;

import com.ecommerce.common.exceptions.ResourceConflictException;
import com.ecommerce.product.dto.ProductCategoryUpdateRequest;
import com.ecommerce.product.dto.ProductCreateRequest;
import com.ecommerce.product.dto.ProductUpdateRequest;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.mapper.ProductMapper;
import com.ecommerce.product.repository.CategoryRepository;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.services.ProductService;
import com.ecommerce.product.specification.ProductSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public Product createProduct(ProductCreateRequest request) {
        if (productRepository.existsBySku(request.sku())) {
            throw new ResourceConflictException("Product with SKU '" + request.sku() + "' already exists.");
        }

        Optional<Category> categoryOptional = categoryRepository.findByCode(request.categoryCode());
        if (categoryOptional.isEmpty()) {
            throw new EntityNotFoundException("Category with code '" + request.categoryCode() + "' not found.");
        }

        Product product = productMapper.toEntity(request, categoryOptional.get());
        return productRepository.save(product);
    }

    @Override
    public Page<Product> findAllProduct(Pageable  pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product findProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Product with id '" + id + "' not found.")
        );

        return product;
    }

    @Override
    public Page<Product> filterProduct(String category, boolean status, Integer minPrice, Integer maxPrice, Pageable pageable) {
       Category category1 = categoryRepository.findByNameIgnoreCase(category).orElseThrow(()-> new EntityNotFoundException("Category '" + category + "' not found."));
       String categoryCode = category1.getCode();
        Specification<Product> spec =
                ProductSpecification.hasCategory(categoryCode)
                        .and(ProductSpecification.isActive(status))
                        .and(ProductSpecification.priceBetween(minPrice, maxPrice));

        return productRepository.findAll(spec,pageable);
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request) {
        Product product = productRepository.findByNameIgnoreCase(request.name()).orElseThrow(()->
                new EntityNotFoundException("Product with name '" + request.name() + "' not found."));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        Product updatedProduct = productRepository.save(product);
        return updatedProduct;
    }

    @Override
    public Product productCategoryUpdate(ProductCategoryUpdateRequest request,Long id) {
        Category category = categoryRepository.findByNameIgnoreCase(request.name()).orElseThrow(()->
                new EntityNotFoundException("Category '" + request.name() + "' not found."));
        String categoryCode = category.getCode();

        Product product = productRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("Product with id '" + id + "' not found."));
        product.setCategory(category);
        Product updatedProduct = productRepository.save(product);
        return updatedProduct;
    }
}

package com.ecommerce.product.services.impl;

import com.ecommerce.common.exceptions.ResourceConflictException;
import com.ecommerce.product.dto.request.ProductCategoryUpdateRequest;
import com.ecommerce.product.dto.request.ProductCreateRequest;
import com.ecommerce.product.dto.request.ProductUpdateRequest;
import com.ecommerce.product.dto.response.ProductResponse;
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
    public ProductResponse createProduct(ProductCreateRequest request) {
        if (productRepository.existsBySku(request.sku())) {
            throw new ResourceConflictException("Product with SKU '" + request.sku() + "' already exists.");
        }

        Optional<Category> categoryOptional = categoryRepository.findByCode(request.categoryCode());
        if (categoryOptional.isEmpty()) {
            throw new EntityNotFoundException("Category with code '" + request.categoryCode() + "' not found.");
        }

        Product product = productMapper.toEntity(request, categoryOptional.get());
        Product savedProduct =  productRepository.save(product);
        ProductResponse productResponse = productMapper.toResponse(savedProduct);
        return productResponse;
    }

    @Override
    public Page<ProductResponse> findAllProduct(Pageable  pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(productMapper::toResponse);
    }

    @Override
    public ProductResponse findProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Product with id '" + id + "' not found.")
        );

        return productMapper.toResponse(product);
    }

    @Override
    public Page<ProductResponse> filterProduct(String category, boolean status, Integer minPrice, Integer maxPrice, Pageable pageable) {
       Category category1 = categoryRepository.findByNameIgnoreCase(category).orElseThrow(()-> new EntityNotFoundException("Category '" + category + "' not found."));
       String categoryCode = category1.getCode();
        Specification<Product> spec =
                ProductSpecification.hasCategory(categoryCode)
                        .and(ProductSpecification.isActive(status))
                        .and(ProductSpecification.priceBetween(minPrice, maxPrice));

        Page<Product> savedProduct = productRepository.findAll(spec,pageable);
        return  savedProduct.map(productMapper::toResponse);
    }

    @Override
    public ProductResponse updateProduct(ProductUpdateRequest request) {
        Product product = productRepository.findByNameIgnoreCase(request.name()).orElseThrow(()->
                new EntityNotFoundException("Product with name '" + request.name() + "' not found."));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public ProductResponse productCategoryUpdate(ProductCategoryUpdateRequest request,Long id) {
        Category category = categoryRepository.findByNameIgnoreCase(request.name()).orElseThrow(()->
                new EntityNotFoundException("Category '" + request.name() + "' not found."));
        String categoryCode = category.getCode();

        Product product = productRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("Product with id '" + id + "' not found."));
        product.setCategory(category);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public String toggleStatus(Long id, boolean b) {
        Product product = productRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("Product with id '" + id + "' not found."));

        product.setIsActive(b);
        productRepository.save(product);
        return "Product activated";
    }
}

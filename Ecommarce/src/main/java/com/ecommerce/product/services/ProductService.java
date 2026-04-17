package com.ecommerce.product.services;

import com.ecommerce.product.dto.request.ProductCreateRequest;
import com.ecommerce.product.dto.request.ProductUpdateRequest;
import com.ecommerce.product.dto.response.ImageUploadResponse;
import com.ecommerce.product.dto.response.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductCreateRequest request);

    List<ProductResponse> getAllProduct(Pageable pageable);

    ProductResponse productDetails(Long id);

    ProductResponse updateProduct(Long id, @Valid ProductUpdateRequest request) throws AccessDeniedException;

    void deleteProduct(Long id) throws AccessDeniedException;

    void toggleStatus(Long id,Boolean isActive);

    List<ProductResponse>  search(Pageable pageable, String keyword);

    ImageUploadResponse uploadImage(Long productId, MultipartFile file) throws AccessDeniedException;


    void updateProductImage(Long productId, MultipartFile file) throws AccessDeniedException;
}

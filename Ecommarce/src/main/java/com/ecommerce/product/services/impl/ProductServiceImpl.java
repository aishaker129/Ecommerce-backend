package com.ecommerce.product.services.impl;

import com.ecommerce.product.dto.request.ProductCreateRequest;
import com.ecommerce.product.dto.request.ProductUpdateRequest;
import com.ecommerce.product.dto.response.ImageUploadResponse;
import com.ecommerce.product.dto.response.ProductResponse;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.entity.Inventory;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.mapper.ProductMapper;
import com.ecommerce.product.repository.CategoryRepository;
import com.ecommerce.product.repository.InventoryRepository;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.services.ProductService;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageUploadService imageUploadService;
    private final ProductMapper mapper;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;

    @Override
    public ProductResponse createProduct(ProductCreateRequest request) {

        if(productRepository.existsBySku(request.sku())){
            throw new EntityExistsException("Product sku already exists.");
        }

        Optional<Category> category = categoryRepository.findByCode(request.categoryCode());

        if(category.isEmpty()){
            throw new EntityNotFoundException("Category not found with this code: "+request.categoryCode());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new EntityNotFoundException("User not found with this username '"+username+"'")
        );

        Product product = mapper.toEntity(request, category.get());
        product.setCreateBy(user.getId());

        ProductResponse response = mapper.toDto(productRepository.save(product));

        Inventory inventory = Inventory.builder()
                .product(product)
                .totalQuantity(0)
                .reservedQuantity(0)
                .build();
        inventoryRepository.save(inventory);
        return response;
    }

    @Override
    public List<ProductResponse> getAllProduct(Pageable pageable) {
        Page<Product> products = productRepository.findByIsActive(true,pageable);
        List<ProductResponse> responses = new ArrayList<>();
        for(var product: products){
            ProductResponse response = mapper.toDto(product);
            responses.add(response);
        }
        return responses;
    }

    @Override
    public ProductResponse productDetails(Long id) {
//        Product product = productRepository.findById(id).orElseThrow(
//                ()-> new EntityNotFoundException("Product not found with this id: "+ id)
//        );

        Product product = productRepository.findAllByIdAndIsActive(id,true).orElseThrow(
                ()-> new EntityNotFoundException("Product not found with this id: "+ id)
        );
        ProductResponse response = mapper.toDto(product);
        return response;
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductUpdateRequest request) throws AccessDeniedException {
        Product oldProduct = productRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Product not found with this id: "+ id)
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new EntityNotFoundException("User not found with this username '"+username+"'")
        );

        if(user.getId() != oldProduct.getCreateBy()){
            throw new AccessDeniedException("You are not allowed to update this product.");
        }

        oldProduct.setName(request.name());
        oldProduct.setDescription(request.description());
        oldProduct.setPrice(request.price());
        oldProduct.setModifiedBy(user.getId());
        ProductResponse response = mapper.toDto(productRepository.save(oldProduct));
        return response;
    }

    @Override
    public void  deleteProduct(Long id) throws AccessDeniedException{
        Product oldProduct = productRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Product not found with this id: "+ id)
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new EntityNotFoundException("User not found with this username '"+username+"'")
        );

        if(user.getId() != oldProduct.getCreateBy()){
            throw new AccessDeniedException("You are not allowed to delete this product.");
        }
        Inventory inventory = inventoryRepository.findByProduct_Id(id).orElseThrow(
                ()-> new EntityNotFoundException("Inventory not found with this product id: "+id)
        );
        imageUploadService.deleteImage(oldProduct.getImagePublicId());
        inventoryRepository.delete(inventory);
        productRepository.delete(oldProduct);
    }

    @Override
    public void toggleStatus(Long id, Boolean isActive) {
        Product oldProduct = productRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Product not found with this id: "+ id)
        );

        oldProduct.setIsActive(isActive);
        productRepository.save(oldProduct);
    }

    @Override
    public List<ProductResponse> search(Pageable pageable, String keyword) {
        Page<Product> products = productRepository.searchActiveProducts(keyword, pageable);
        if(products.isEmpty()){
            throw new EntityNotFoundException("Product not found with this keyword.");
        }
        List<ProductResponse> responses = new ArrayList<>();

        for(var product: products){
            ProductResponse response = mapper.toDto(product);
            responses.add(response);
        }
        return responses;
    }

    @Override
    public ImageUploadResponse uploadImage(Long productId, MultipartFile file) throws AccessDeniedException{
        Product product = productRepository.findById(productId).orElseThrow(
                ()-> new EntityNotFoundException("Product not found with this id: "+ productId)
        );
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new EntityNotFoundException("User not found with this username '"+username+"'")
        );

        if(user.getId() != product.getCreateBy()){
            throw new AccessDeniedException("You are not allowed to upload this product image.");
        }

        validateImageFile(file);

        ImageUploadResponse response = imageUploadService.uploadImage(file,"CampusKart/products");
        product.setImageUrl(response.imageUrl());
        product.setImagePublicId(response.imagePublicId());
        product.setModifiedBy(user.getId());
        productRepository.save(product);
        return response;
    }


    @Override
    public void updateProductImage(Long productId, MultipartFile file) throws AccessDeniedException{
        Product product = productRepository.findById(productId).orElseThrow(
                ()-> new EntityNotFoundException("Product not found with this id: "+ productId)
        );
        if(product.getImagePublicId() != null){
            imageUploadService.deleteImage(product.getImagePublicId());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new EntityNotFoundException("User not found with this username '"+username+"'")
        );

        if(user.getId() != product.getCreateBy()){
            throw new AccessDeniedException("You are not allowed to update this product.");
        }

        validateImageFile(file);

        ImageUploadResponse response = imageUploadService.uploadImage(file,"CampusKart/products");
        product.setImageUrl(response.imageUrl());
        product.setImagePublicId(response.imagePublicId());
        product.setModifiedBy(user.getId());
        productRepository.save(product);
    }

    private void validateImageFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Image is required");
        }

        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files allowed");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Max size is 2MB");
        }

    }
}

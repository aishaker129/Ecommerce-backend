package com.ecommerce.product.controller;

import com.ecommerce.common.constants.ApiEndPoints;
import com.ecommerce.common.dto.response.ApiResponse;
import com.ecommerce.product.dto.request.InventoryRequest;
import com.ecommerce.product.dto.request.ProductCreateRequest;
import com.ecommerce.product.dto.request.ProductUpdateRequest;
import com.ecommerce.product.dto.response.ImageUploadResponse;
import com.ecommerce.product.dto.response.InventoryResponse;
import com.ecommerce.product.dto.response.ProductResponse;
import com.ecommerce.product.entity.Inventory;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.services.InventoryService;
import com.ecommerce.product.services.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ApiEndPoints.ProductAdmin.BASE_PRODUCT)
@Tag(
        name = "Products",
        description = "Administrative operation for product."
)
public class ProductController {
    private final ProductService productService;
    private final InventoryService inventoryService;

    @Operation(
            summary = "Create product.",
            description = "Administrative operation for product.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "201",
                            description = "Product created successfully.",
                            content = @Content(
                                    schema = @Schema(implementation = Product.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Validation error.",
                            content = @Content
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "409",
                            description = "Sku already exists.",
                            content = @Content
                    )
            }
    )
    @PreAuthorize("hasAuthority(T(com.ecommerce.user.enums.PermissionType).CREATE_PRODUCT.name())")
    @PostMapping(value = ApiEndPoints.ProductAdmin.ADD_PRODUCT)
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductCreateRequest request
    ) throws JsonProcessingException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        auth.getAuthorities().forEach(a ->
                System.out.println("AUTH: " + a.getAuthority())
        );
        return ResponseEntity.ok(ApiResponse.success(productService.createProduct(request)));
    }

    @PreAuthorize("permitAll()")
    @GetMapping(ApiEndPoints.ProductAdmin.ALL_PRODUCT)
    public ResponseEntity<ApiResponse<List<ProductResponse>>> allProducts(
            @RequestParam(name = "page",defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ){
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(ApiResponse.success(productService.getAllProduct(pageable)));
    }


    @Operation(
            summary = "Update product inventory.",
            description = "Administrative operation to adjust product stock levels and synchronize inventory records.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Inventory updated successfully.",
                            content = @Content(
                                    schema = @Schema(implementation = Inventory.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Invalid request parameters or validation error.",
                            content = @Content
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Product not found.",
                            content = @Content
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Internal server error during inventory update.",
                            content = @Content
                    )
            }
    )
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasAnyRole('ADMIN','SELLER') and hasAuthority(T(com.ecommerce.user.enums.PermissionType).UPDATE_INVENTORY.name())")
    @PutMapping(ApiEndPoints.ProductAdmin.PRODUCT_INVENTORY)
    public ResponseEntity<ApiResponse<Void>> updateStock(@PathVariable Long productId, @Valid @RequestBody InventoryRequest request) throws AccessDeniedException{
        inventoryService.updateStock(productId,request);
        return ResponseEntity.ok(ApiResponse.success("Product stock updated."));
    }

    @Operation(
            summary = "Product Details.",
            description = "Operation for showing a specific product details.",
            security = @SecurityRequirement(name = ""),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Product Fetch successfully.",
                            content = @Content(
                                    schema = @Schema(implementation = Product.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Product not found.",
                            content = @Content
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Internal server error during inventory update.",
                            content = @Content
                    )
            }
    )
    @PreAuthorize("permitAll()")
    @GetMapping(ApiEndPoints.ProductAdmin.PRODUCT_DETAILS)
    public ResponseEntity<ApiResponse<ProductResponse>> productDetails(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(ApiResponse.success(productService.productDetails(id)));
    }

    @Operation(
            summary = "Update product.",
            description = "Administrative operation for product update.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Product update successfully.",
                            content = @Content(
                                    schema = @Schema(implementation = Product.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Product not found.",
                            content = @Content
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Internal server error during inventory update.",
                            content = @Content
                    )
            }
    )
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasAnyRole('ADMIN','SELLER') and hasAuthority(T(com.ecommerce.user.enums.PermissionType).UPDATE_PRODUCT.name())")
    @PutMapping(ApiEndPoints.ProductAdmin.UPDATE_PRODUCT)
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest request
    ) throws AccessDeniedException {
        return ResponseEntity.ok(ApiResponse.success(productService.updateProduct(id,request)));
    }

    @Operation(
            summary = "Delete product.",
            description = "Administrative operation for deleting a product.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Product delete successfully.",
                            content = @Content(
                                    schema = @Schema(implementation = Product.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Product not found.",
                            content = @Content
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Internal server error during inventory update.",
                            content = @Content
                    )
            }
    )
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasAuthority(T(com.ecommerce.user.enums.PermissionType).DELETE_PRODUCT.name())")
    @DeleteMapping(ApiEndPoints.ProductAdmin.DELETE_PRODUCT)
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) throws AccessDeniedException{
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product Deleted Successfully."));
    }

    @Operation(
            summary = "Toggle product status.",
            description = "Administrative operation for change product status.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Product status changed successfully.",
                            content = @Content(
                                    schema = @Schema(implementation = Product.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Product not found.",
                            content = @Content
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Internal server error during inventory update.",
                            content = @Content
                    )
            }
    )
    @PreAuthorize("hasRole('SUPER_ADMIN') or (hasAnyRole('ADMIN') and hasAuthority(T(com.ecommerce.user.enums.PermissionType).TOGGLE_PRODUCT_STATUS.name()))")
    @PatchMapping(ApiEndPoints.ProductAdmin.UPDATE_PRODUCT_STATUS)
    public ResponseEntity<ApiResponse<Void>> toggleProductStatus(@PathVariable Long id, @RequestParam @NonNull Boolean isActive){
        productService.toggleStatus(id, isActive);
        return ResponseEntity.ok(ApiResponse.success("Changed product status."));
    }

    @Operation(
            summary = "Search product by keyword.",
            description = "Operation for search product by using keyword.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Show product which containing keyword.",
                            content = @Content(schema = @Schema(implementation = ProductResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Product not found.",
                            content = @Content
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Internal server error during inventory update.",
                            content = @Content
                    )
            }
    )
    @PreAuthorize("permitAll()")
    @GetMapping(ApiEndPoints.ProductAdmin.SEARCH_PRODUCT)
    public ResponseEntity<ApiResponse<List<ProductResponse> >> searchProduct(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "page",defaultValue = "0") Integer page,
            @RequestParam(name = "size",defaultValue = "10") Integer size
    ){
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(ApiResponse.success(productService.search(pageable,keyword)));
    }

    @Operation(
            summary = "Upload product image.",
            description = "Operation for upload product image.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Product image upload successfully.",
                            content = @Content(
                                    schema = @Schema(implementation = Product.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Product not found.",
                            content = @Content
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Internal server error during inventory update.",
                            content = @Content
                    )
            }
    )
    @PreAuthorize(
            "hasAuthority(T(com.ecommerce.user.enums.PermissionType).UPLOAD_PRODUCT_IMAGE.name())"
    )
    @PostMapping(value = ApiEndPoints.ProductAdmin.PRODUCT_IMAGE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ImageUploadResponse>> uploadImage(
            @PathVariable Long productId,
            @RequestParam MultipartFile file
    ) throws AccessDeniedException{
        return ResponseEntity.ok(ApiResponse.success(productService.uploadImage(productId,file)));
    }

    @Operation(
            summary = "Update product image.",
            description = "Administrative operation for delete product image.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Delete product image successfulley.",
                            content = @Content(schema = @Schema(implementation = Product.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Product not found.",
                            content = @Content
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Internal server error during inventory update.",
                            content = @Content
                    )
            }
    )
    @PreAuthorize(
            "hasRole('SUPER_ADMIN') OR (hasAnyRole('ADMIN','SELLER') AND hasAuthority(T(com.ecommerce.user.enums.PermissionType).UPLOAD_PRODUCT_IMAGE.name()))"
    )
    @PatchMapping(ApiEndPoints.ProductAdmin.PRODUCT_IMAGE)
    public ResponseEntity<ApiResponse<Void>> updateProductImage(
            @PathVariable Long productId,
            @RequestParam MultipartFile file
    ) throws AccessDeniedException{
        productService.updateProductImage(productId,file);
        return ResponseEntity.ok(ApiResponse.success("Product image updated successfully."));
    }

    @PreAuthorize(
            "hasRole('SUPER_ADMIN') OR (hasAnyRole('ADMIN','SELLER') AND hasAuthority(T(com.ecommerce.user.enums.PermissionType).UPLOAD_PRODUCT_IMAGE.name()))"
    )
    @GetMapping(ApiEndPoints.ProductAdmin.CHECK_PRODUCT_STOCK)
    public ResponseEntity<ApiResponse<InventoryResponse>> productStock(@PathVariable("productId") Long productId){
        return ResponseEntity.ok(ApiResponse.success(inventoryService.productStock(productId)));
    }

}

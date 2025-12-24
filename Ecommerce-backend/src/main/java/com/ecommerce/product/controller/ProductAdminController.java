package com.ecommerce.product.controller;


import com.ecommerce.common.constants.ApiEndpoint;
import com.ecommerce.common.dto.ApiResponse;
import com.ecommerce.common.dto.PaginatedResponse;
import com.ecommerce.product.dto.request.ProductCategoryUpdateRequest;
import com.ecommerce.product.dto.request.ProductCreateRequest;
import com.ecommerce.product.dto.request.ProductUpdateRequest;
import com.ecommerce.product.dto.response.ProductResponse;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiEndpoint.ProductAdmin.BASE_PRODUCT_ADMIN)
@RequiredArgsConstructor
@Tag(
        name = "Product Admin",
        description = "Administrative operation for managing product"
)
public class ProductAdminController {

    private  final ProductService productService;

    @Operation(
            summary = "Create a new product",
            description = "Creates a new product in the system. Validates SKU uniqueness and category existence.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Product created successfully",
                            content = @Content(schema = @Schema(implementation = Product.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Validation error or bad request",
                            content = @Content
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "409",
                            description = "Duplicate SKU or resource conflict",
                            content = @Content
                    )
            }
    )
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(productService.createProduct(request)));
    }

    // TODO: Implement endpoint to retrieve product details by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.findProductById(id)));
    }

    // TODO: Implement endpoint to retrieve all product details
    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<ProductResponse>>> getProduct(
            @RequestParam(name = "page",defaultValue = "0") int page,
            @RequestParam(name = "size",defaultValue = "10") int size
            ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success(PaginatedResponse.of(productService.findAllProduct(pageable))));
    }

    // TODO: Implement paginated product listing with filters
    //      (category, active status, price range)

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<PaginatedResponse<ProductResponse>>> getProduct(
            @RequestParam(name = "category",defaultValue = "NAN",required = false) String category,
            @RequestParam(name = "status",defaultValue = "active",required = false) boolean status,
            @RequestParam(name = "minPrice",defaultValue = "0",required = false) Integer minPrice,
            @RequestParam(name = "maxPrice",defaultValue = "100000",required = false) Integer maxPrice,
            @RequestParam(name = "page",defaultValue = "0") Integer page,
            @RequestParam(name = "size",defaultValue = "10")  Integer size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success(PaginatedResponse.of(productService.filterProduct(category,status,minPrice,maxPrice,pageable))));
    }

    // TODO: Implement endpoint to update product information
    //      (name, price, description)

    @PutMapping
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@Valid @RequestBody ProductUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(productService.updateProduct(request)));
    }

    // TODO: Implement functionality to reassign a product to a different category

    @PutMapping("/update-category/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateCategory(
            @Valid @RequestBody ProductCategoryUpdateRequest request,@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.productCategoryUpdate(request,id)));
    }

    // TODO: Implement endpoint to activate or deactivate a product
    //      (soft delete via isActive flag)

    @PutMapping("/{id}/active")
    public ResponseEntity<ApiResponse<String>> updateProductStatisActive(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.toggleStatus(id,true)));
    }

    @PutMapping("/{id}/deactive")
    public ResponseEntity<ApiResponse<String>> updateProductStatusDeactive(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.toggleStatus(id,false)));
    }

    // TODO: Implement endpoint to permanently delete a product
}

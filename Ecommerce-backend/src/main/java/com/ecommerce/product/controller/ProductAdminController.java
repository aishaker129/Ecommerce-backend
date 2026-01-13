package com.ecommerce.product.controller;


import com.ecommerce.common.constants.ApiEndpoint;
import com.ecommerce.common.dto.resonse.ApiResponse;
import com.ecommerce.common.dto.resonse.PaginatedResponse;
import com.ecommerce.product.dto.request.InventoryUpdateRequest;
import com.ecommerce.product.dto.request.ProductCategoryUpdateRequest;
import com.ecommerce.product.dto.request.ProductCreateRequest;
import com.ecommerce.product.dto.request.ProductUpdateRequest;
import com.ecommerce.product.dto.response.ProductResponse;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.services.InventoryService;
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
    private final InventoryService inventoryService;

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
    @Operation(
            summary = "Get product by ID",
            description = "Retrieves details of a product by its unique identifier.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Product retrieved successfully",
                            content = @Content(schema = @Schema(implementation = Product.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.findProductById(id)));
    }

    // TODO: Implement endpoint to retrieve all product details
    @Operation(
            summary = "List Product",
            description = "Retrieves a paginated list of Product.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Product listed successfully",
                            content = @Content(schema = @Schema(implementation = PaginatedResponse.class))
                    )
            }
    )
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

    @Operation(
            summary = "Filer Product list",
            description = "Retrieves filtered a paginated list of Product.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Product listed successfully",
                            content = @Content(schema = @Schema(implementation = PaginatedResponse.class))
                    )
            }
    )
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

    @Operation(
            summary = "Update category details",
            description = "Updates an existing Product.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Product updated successfully",
                            content = @Content(schema = @Schema(implementation = Product.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Validation error",
                            content = @Content
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable Long id,@Valid @RequestBody ProductUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(productService.updateProduct(id, request)));
    }

    // TODO: Implement functionality to reassign a product to a different category


    @Operation(
            summary = "Update Product details",
            description = "Updates the Category  of an existing Product.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Product category updated successfully",
                            content = @Content(schema = @Schema(implementation = Product.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Validation error",
                            content = @Content
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content
                    )
            }
    )
    @PutMapping("/update-category/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateCategory(
            @Valid @RequestBody ProductCategoryUpdateRequest request,@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.productCategoryUpdate(request,id)));
    }

    // TODO: Implement endpoint to activate or deactivate a product
    //      (soft delete via isActive flag)

    @Operation(
            summary = "Activate or deactivate product",
            description = "Toggles the active status of a product (soft delete via isActive flag).",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Product status updated successfully",
                            content = @Content(schema = @Schema(implementation = Product.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content
                    )
            }
    )
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<String>> updateProductStatisActive(@PathVariable Long id,@RequestParam Boolean isActive) {
        return ResponseEntity.ok(ApiResponse.success(productService.toggleStatus(id,isActive)));
    }

    // TODO: Implement endpoint to permanently delete a product

    @Operation(
            summary = "Delete Product permanently",
            description = "Deletes a product permanently from the system.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "204",
                            description = "Product deleted successfully"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(productService.deleteProduct(id)));
    }

    @PutMapping(ApiEndpoint.ProductAdmin.PRODUCT_INVENTORY)
    public ResponseEntity<ApiResponse<Void>> updateStock(@PathVariable Long productId, @Valid @RequestBody InventoryUpdateRequest request){
        inventoryService.updateStock(productId,request);
        return ResponseEntity.ok(ApiResponse.success("Stock updated successfully"));
    }
}

package com.ecommerce.product.controller;

import com.ecommerce.common.constants.ApiEndpoint;
import com.ecommerce.common.dto.resonse.ApiResponse;
import com.ecommerce.common.dto.resonse.PaginatedResponse;
import com.ecommerce.product.dto.request.CategoryCreateRequest;
import com.ecommerce.product.dto.request.CategoryUpdateRequest;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiEndpoint.CategoryAdmin.BASE_CATEGORY)
@RequiredArgsConstructor
@Tag(
        name = "Category admin",
        description = "Administrative operation for category admin"
)
public class CategoryAdminController {
    private  final CategoryService categoryService;

    @Operation(
            summary = "Create new Category",
            description = "Create new Category in the system. Category code must be unique",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Category created successfully",
                            content = @Content(
                                    schema = @Schema(implementation = Category.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Validation error",
                            content = @Content
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "409",
                            description = "Category already exists",
                            content = @Content
                    )
            }
    )
    @PostMapping
    @PreAuthorize("hasAuthority(T(com.ecommerce.user.enums.PermissionType).CREATE_CATEGORY.name())")
    public ResponseEntity<ApiResponse<Category>> createCategory(@Valid @RequestBody CategoryCreateRequest request){
        return ResponseEntity.ok(ApiResponse.success(categoryService.create(request)));
    }

    @Operation(
            summary = "Get category by ID",
            description = "Retrieves details of a category by its unique identifier.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Category retrieved successfully",
                            content = @Content(schema = @Schema(implementation = Category.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Category not found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.ecommerce.user.enums.PermissionType).VIEW_CATEGORY.name())")
    public ResponseEntity<ApiResponse<Category>> findAllCategory(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(categoryService.getCategoryById(id)));
    }

    @Operation(
            summary = "List categories",
            description = "Retrieves a paginated list of categories.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Categories listed successfully",
                            content = @Content(schema = @Schema(implementation = PaginatedResponse.class))
                    )
            }
    )
    @GetMapping
    @PreAuthorize("hasAuthority(T(com.ecommerce.user.enums.PermissionType).VIEW_CATEGORY.name())")
    public ResponseEntity<ApiResponse<PaginatedResponse<Category>>> getAllCategory(
            @RequestParam(name = "page",defaultValue = "0") Integer page,
            @RequestParam(name = "size",defaultValue = "10") Integer size
    ){
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(ApiResponse.success(PaginatedResponse.of(categoryService.getAll(pageable))));
    }

    @Operation(
            summary = "Update category details",
            description = "Updates the name and description of an existing category.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Category updated successfully",
                            content = @Content(schema = @Schema(implementation = Category.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Validation error",
                            content = @Content
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Category not found",
                            content = @Content
                    )
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.ecommerce.user.enums.PermissionType).UPDATE_CATEGORY.name())")
    public ResponseEntity<ApiResponse<Category>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateRequest request
    ){
        return ResponseEntity.ok(ApiResponse.success(categoryService.updateCategory(id,request)));
    }

    @Operation(
            summary = "Activate or deactivate category",
            description = "Toggles the active status of a category (soft delete via isActive flag).",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Category status updated successfully",
                            content = @Content(schema = @Schema(implementation = Category.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Category not found",
                            content = @Content
                    )
            }
    )
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority(T(com.ecommerce.user.enums.PermissionType).TOGGLE_CATEGORY_STATUS.name())")
    public ResponseEntity<ApiResponse<Category>> toggleCategoryStatus(
            @PathVariable Long id,
            @RequestParam Boolean isActive)
    {
        return ResponseEntity.ok(ApiResponse.success(categoryService.toggleCategoryStatus(id,isActive)));
    }

    @Operation(
            summary = "Delete category permanently",
            description = "Deletes a category permanently from the system.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "204",
                            description = "Category deleted successfully"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Category not found",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.ecommerce.user.enums.PermissionType).DELETE_CATEGORY.name())")
    public ResponseEntity<ApiResponse<String>> deleteCategoryById(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(categoryService.deleteCategory(id)));
    }

}

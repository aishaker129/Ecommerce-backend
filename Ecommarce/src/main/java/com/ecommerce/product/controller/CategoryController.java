package com.ecommerce.product.controller;

import com.ecommerce.common.constants.ApiEndPoints;
import com.ecommerce.common.dto.response.ApiResponse;
import com.ecommerce.product.dto.request.CategoryUpdateRequest;
import com.ecommerce.product.dto.request.CreateCategoryRequest;
import com.ecommerce.product.dto.response.CategoryResponse;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiEndPoints.CategoryAdmin.BASE_CATEGORY)
@Tag(
        name = "Category Admin",
        description = "Administrative operation for category admin"
)
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(
            summary = "Create new category",
            description = "Create new category in the system. Category code must be unique.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "201",
                            description = "Category created successfully.",
                            content = @Content(
                                    schema = @Schema(implementation = Category.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Validation error.",
                            content = @Content
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "409",
                            description = "Category already exists.",
                            content = @Content
                    )
            }
    )
    @PreAuthorize("hasRole('SUPER_ADMIN') or (hasRole('ADMIN') and hasAuthority(T(com.ecommerce.user.enums.PermissionType).CREATE_CATEGORY.name()))")
    @PostMapping(ApiEndPoints.CategoryAdmin.ADD_CATEGORY)
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CreateCategoryRequest request){
        return ResponseEntity.ok(ApiResponse.success(categoryService.createCategory(request)));
    }

    @Operation(
            summary = "Update existsing category.",
            description = "Administrative operation for update existing category",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Category updated successfully.",
                            content = @Content(
                                    schema = @Schema(implementation = Category.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Category not found",
                            content = @Content
                    )
            }
    )
    @PreAuthorize("hasRole('SUPER_ADMIN') or (hasRole('ADMIN') and hasAuthority(T(com.ecommerce.user.enums.PermissionType).UPDATE_CATEGORY.name()))")
    @PutMapping(ApiEndPoints.CategoryAdmin.UPDATE_CATEGORY)
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@Valid  @PathVariable Long id, @RequestBody CategoryUpdateRequest request){
        return ResponseEntity.ok(ApiResponse.success(categoryService.update(id,request)));
    }

    @Operation(
            summary = "Fetch category list.",
            description = "Administrative operation for fatch category list.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Category list fetch successflly.",
                            content = @Content(
                                    schema = @Schema(implementation = Category.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Category not found.",
                            content = @Content
                    )
            }
    )
    @PreAuthorize("permitAll()")
    @GetMapping(ApiEndPoints.CategoryAdmin.VIEW_CATEGORY)
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> fetchCategory(
            @RequestParam(name = "page",defaultValue = "0") Integer page,
            @RequestParam(name = "size",defaultValue = "10") Integer size
    ){
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(ApiResponse.success(categoryService.fetchCategoryList(pageable)));
    }

    @Operation(
            summary = "Toggle category status.",
            description = "Administrative operation for category status",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Updated category status",
                            content = @Content(
                                    schema = @Schema(implementation = Category.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "validation error",
                            content = @Content
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "category not found",
                            content = @Content
                    )
            }
    )
    @PreAuthorize("hasRole('SUPER_ADMIN') or (hasRole('ADMIN') and hasAuthority(T(com.ecommerce.user.enums.PermissionType).TOGGLE_CATEGORY_STATUS.name()))")
    @PatchMapping(ApiEndPoints.CategoryAdmin.TOGGLE_CATEGORY_STATUS)
    public ResponseEntity<ApiResponse<CategoryResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam @NonNull Boolean isActive
    ){
        return ResponseEntity.ok(ApiResponse.success(categoryService.toggleCategoryStatus(id,isActive)));
    }

    @Operation(
            summary = "Delete category.",
            description = "Administrative operation for delete category with id",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Category deleted successfully.",
                            content = @Content(
                                    schema = @Schema(implementation = Category.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "403",
                            description = "Category can delete only administrative user.",
                            content = @Content
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Category not found.",
                            content = @Content
                    )
            }
    )
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasAuthority(T(com.ecommerce.user.enums.PermissionType).DELETE_CATEGORY.name())")
    @DeleteMapping(ApiEndPoints.CategoryAdmin.DELETE_CATEGORY)
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @PathVariable Long id
    ){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully."));
    }
}

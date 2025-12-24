package com.ecommerce.product.controller;

import com.ecommerce.common.constants.ApiEndpoint;
import com.ecommerce.common.dto.ApiResponse;
import com.ecommerce.product.dto.request.CategoryCreateRequest;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiEndpoint.CategoryAdmin.BASE_CATEGORY_ADMIN)
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
    public ResponseEntity<ApiResponse<Category>> createCategory(@Valid @RequestBody CategoryCreateRequest request){
        return ResponseEntity.ok(ApiResponse.success(categoryService.create(request)));
    }
}

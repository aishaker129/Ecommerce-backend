package com.ecommerce.product.mapper;

import com.ecommerce.product.dto.request.CategoryUpdateRequest;
import com.ecommerce.product.dto.request.CreateCategoryRequest;
import com.ecommerce.product.dto.response.CategoryResponse;
import com.ecommerce.product.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    Category toEntity(CreateCategoryRequest request);
    CategoryResponse toDto(Category category);

    Category toEntity(CategoryUpdateRequest request);

}

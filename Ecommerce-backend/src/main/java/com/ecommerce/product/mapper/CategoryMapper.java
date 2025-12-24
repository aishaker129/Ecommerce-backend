package com.ecommerce.product.mapper;

import com.ecommerce.product.dto.request.CategoryCreateRequest;
import com.ecommerce.product.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    Category toEntity(CategoryCreateRequest request);
}

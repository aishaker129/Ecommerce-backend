package com.ecommerce.product.mapper;

import com.ecommerce.product.dto.request.ProductCreateRequest;
import com.ecommerce.product.dto.response.ProductResponse;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "category", source = "category")
    Product toEntity(ProductCreateRequest request, Category category);

    @Mapping(target = "categoryId", source = "category.id") // if createdBy is User entity
    ProductResponse toResponse(Product product);
}

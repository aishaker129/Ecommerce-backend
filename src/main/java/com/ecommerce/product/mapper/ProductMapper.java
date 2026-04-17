package com.ecommerce.product.mapper;

import com.ecommerce.product.dto.request.ProductCreateRequest;
import com.ecommerce.product.dto.request.ProductUpdateRequest;
import com.ecommerce.product.dto.response.ProductResponse;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    // Create
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "category", source = "category")
    Product toEntity(ProductCreateRequest request, Category category);

    // Response mapping
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "createAt", source = "createAt")
    @Mapping(target = "createdByName", expression = "java(mapCreatedBy(product))")
    ProductResponse toDto(Product product);

    // Update
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true) // important!
    Product toEntity(ProductUpdateRequest request);

    // custom method
    default String mapCreatedBy(Product product) {
        return product.getCreateBy() != null
                ? "User-" + product.getCreateBy()
                : "System";
    }
}

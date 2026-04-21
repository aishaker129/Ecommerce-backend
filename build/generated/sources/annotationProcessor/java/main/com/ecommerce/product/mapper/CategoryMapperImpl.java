package com.ecommerce.product.mapper;

import com.ecommerce.product.dto.request.CategoryUpdateRequest;
import com.ecommerce.product.dto.request.CreateCategoryRequest;
import com.ecommerce.product.dto.response.CategoryResponse;
import com.ecommerce.product.entity.Category;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T02:19:37+0600",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.3.1.jar, environment: Java 21.0.10 (Ubuntu)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public Category toEntity(CreateCategoryRequest request) {
        if ( request == null ) {
            return null;
        }

        Category.CategoryBuilder category = Category.builder();

        category.name( request.name() );
        category.code( request.code() );

        return category.build();
    }

    @Override
    public CategoryResponse toDto(Category category) {
        if ( category == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String code = null;
        LocalDateTime createAt = null;
        LocalDateTime updateAt = null;

        id = category.getId();
        name = category.getName();
        code = category.getCode();
        createAt = category.getCreateAt();
        updateAt = category.getUpdateAt();

        CategoryResponse categoryResponse = new CategoryResponse( id, name, code, createAt, updateAt );

        return categoryResponse;
    }

    @Override
    public Category toEntity(CategoryUpdateRequest request) {
        if ( request == null ) {
            return null;
        }

        Category.CategoryBuilder category = Category.builder();

        category.name( request.name() );

        return category.build();
    }
}

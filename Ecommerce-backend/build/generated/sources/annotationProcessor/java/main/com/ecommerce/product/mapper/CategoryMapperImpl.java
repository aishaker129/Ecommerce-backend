package com.ecommerce.product.mapper;

import com.ecommerce.product.dto.request.CategoryCreateRequest;
import com.ecommerce.product.entity.Category;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-24T03:09:43+0600",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public Category toEntity(CategoryCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Category category = new Category();

        category.setName( request.name() );
        category.setCode( request.code() );

        return category;
    }
}

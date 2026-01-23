package com.ecommerce.product.mapper;

import com.ecommerce.product.dto.request.ProductCreateRequest;
import com.ecommerce.product.dto.response.ProductResponse;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.entity.Product;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-24T03:09:44+0600",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toEntity(ProductCreateRequest request, Category category) {
        if ( request == null && category == null ) {
            return null;
        }

        Product product = new Product();

        if ( request != null ) {
            product.setName( request.name() );
            product.setSku( request.sku() );
            product.setDescription( request.description() );
            product.setPrice( request.price() );
            product.setImageUrl( request.imageUrl() );
        }
        if ( category != null ) {
            product.setCategory( category );
            product.setIsActive( category.getIsActive() );
        }

        return product;
    }

    @Override
    public ProductResponse toResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        Long categoryId = null;
        Long id = null;
        String sku = null;
        String name = null;
        String description = null;
        Double price = null;
        Boolean isActive = null;

        categoryId = productCategoryId( product );
        id = product.getId();
        sku = product.getSku();
        name = product.getName();
        description = product.getDescription();
        price = product.getPrice();
        isActive = product.getIsActive();

        LocalDateTime createdAt = null;
        Long createdBy = null;

        ProductResponse productResponse = new ProductResponse( id, sku, name, description, price, isActive, categoryId, createdAt, createdBy );

        return productResponse;
    }

    private Long productCategoryId(Product product) {
        Category category = product.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getId();
    }
}

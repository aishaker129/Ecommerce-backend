package com.ecommerce.product.specification;

import com.ecommerce.product.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> hasCategory(String categoryCode) {
        return (root, query, cb) -> {
            if (categoryCode == null) return null;
            return cb.equal(root.get("category").get("code"), categoryCode);
        };
    }

    public static Specification<Product> isActive(Boolean isActive) {
        return (root, query, cb) -> {
            if (isActive == null) return null;
            return cb.equal(root.get("isActive"), isActive);
        };
    }

    public static Specification<Product> priceBetween(
            Integer minPrice,
            Integer maxPrice
    ) {
        return (root, query, cb) -> {
            if (minPrice == null && maxPrice == null) return null;

            if (minPrice != null && maxPrice != null) {
                return cb.between(root.get("price"), minPrice, maxPrice);
            }
            if (minPrice != null) {
                return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
            }
            return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }
}

package com.ecommerce.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionType {
    // Product Admin
    CREATE_PRODUCT("Create Product"),
    UPDATE_PRODUCT("Update product"),
    DELETE_PRODUCT("Delete Product"),
    UPDATE_INVENTORY("Update Inventory"),
    TOGGLE_PRODUCT_STATUS("Toggle Product Inventory"),
    UPLOAD_PRODUCT_IMAGE("Upload Product Image"),
    UPDATE_PRODUCT_IMAGE("Update Product Image"),
    CHECK_PRODUCT_STOCK("Check Product Stock"),

    // Category Admin
    CREATE_CATEGORY("Create Category"),
    UPDATE_CATEGORY("Update Category"),
    DELETE_CATEGORY("Delete Category"),
    TOGGLE_CATEGORY_STATUS("Toggle Category Status"),

    // Customer / General
    UPDATE_PROFILE("Update Profile"),
    VIEW_PRODUCT("View product details"),
    VIEW_PRODUCTS("View Products"),
    VIEW_CATEGORY("View category"),
    VIEW_CART("View Cart"),
    ADD_TO_CART("Add To Cart"),
    SEARCH_PRODUCT("Searech product"),
    UPDATE_CART("Update cart item"),
    DELETE_CART("Delete user cart"),
    CLEAR_CART("Clear user cart items"),
    UPDATE_PROFILE_IMAGE("Update Profle image"),
    UPLOAD_PROFILE_IMAGE("Upload profile image"),

    // Payments
    CHECKOUT("Checkout"),
    PAYMENT_SUCCESS("Payment Success"),
    PAYMENT_CANCEL("Payment Cancel");

    private final String displayValue;
}

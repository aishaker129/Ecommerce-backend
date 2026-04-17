package com.ecommerce.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiEndPoints {
    private static final String API_VERSION = "/api/v1";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ProductAdmin{
        public static final String BASE_PRODUCT = API_VERSION + "/products";
        public static final String PRODUCT_INVENTORY = "/{productId}/inventory";
        public static final String ADD_PRODUCT = "/add-product";
        public static final String ALL_PRODUCT = "/product-list";
        public static final String PRODUCT_DETAILS = "/{id}";
        public static final String UPDATE_PRODUCT = "/{id}/update-product";
        public static final String DELETE_PRODUCT = "/{id}/delete-product";
        public static final String UPDATE_PRODUCT_STATUS = "/{id}/status";
        public static final String SEARCH_PRODUCT = "/keyword";
        public static final String PRODUCT_IMAGE = "/{productId}/image";
        public static final String CHECK_PRODUCT_STOCK = "/stock/{productId}";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CategoryAdmin{
        public static final String BASE_CATEGORY = API_VERSION +"/categories";
        public static final String ADD_CATEGORY = "/add-category";
        public static final String DELETE_CATEGORY = "/{id}/delete-category";
        public static final String UPDATE_CATEGORY = "/{id}/update-category";
        public static final String VIEW_CATEGORY = "/view-category";
        public static final String TOGGLE_CATEGORY_STATUS ="/{id}/toggle_category_status";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Cart {
        public static final String BASE_CART = API_VERSION + "/cart";
        public static final String ADD_TO_CART ="/add-to-cart";
        public static final String VIEW_CART = "/carts/{userId}";
        public static final String UPDATE_CART = "/update/{cartItemId}";
        public static final String DELETE_CART = "delete/{cartItemId}";

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class OrderApi{
        public static final String BASE_ORDER = API_VERSION + "/order";
        public static final String CREATE_ORDER = "/create";
        public static final String VIEW_ORDER = "/{orderId}";
        public static final String VIEW_USER_ORDER = "/user/{userId}";
        public static final String UPDATE_ORDER = "/update/{orderId}";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Payment{
        public static final String BASE_PAYMENT = API_VERSION + "/payment";
        public static final String CHECKOUT = "/checkout";
        public static final String SUCCESS = "/success";
        public static final String CANCEL = "/cancel";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Webhook{
        public static final String BASE_WEBHOOK = API_VERSION + "/webhook";
        public static final String HANDLE_PAYMENT = "/stripe";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Auth{
        public static final String BASE_AUTH = API_VERSION +"/auth";
        public static final String LOGIN = "/login";
        public static final String REGISTER = "/register";
        public static final String LOGOUT = "/logout";
        public static final String TOKEN_REFRESH = "/refresh";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class User{
        public static final String BASE_USER = API_VERSION + "/users";
        public static final String PROFILE = "/profile/{username}";
        public static final String UPLOAD_PROFILE_IMAGE = "upload-image";
        public static final String UPDATE_PROFILE_IMAGE = "/update-image";
    }
}

package com.ecommerce.cart.controller;

import com.ecommerce.cart.dto.request.CartRequest;
import com.ecommerce.cart.dto.response.CartResponse;
import com.ecommerce.cart.entity.Cart;
import com.ecommerce.cart.services.CartService;
import com.ecommerce.common.constants.ApiEndpoint;
import com.ecommerce.common.dto.resonse.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiEndpoint.CartAdmin.BASE_CART)
@RequiredArgsConstructor
@Tag(
        name = "Cart",
        description = "Operations for managing user shopping carts"
)
public class CartController {

    private final CartService cartService;

    @Operation(
            summary = "Add or update a cart item",
            description = """
                    Adds a product to the user's cart or updates its quantity if it already exists.
                    If quantity is set to 0, the product will be removed from the cart.
                    A new cart is automatically created if the user does not have one yet.
                    """,
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Item successfully added or updated",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Validation error or bad request",
                            content = @Content
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content
                    )
            }
    )
    @PostMapping(ApiEndpoint.CartAdmin.ADD_CART_ITEM)
    @PreAuthorize("hasAuthority(T(com.ecommerce.user.enums.PermissionType).ADD_TO_CART.name())")
    public ResponseEntity<ApiResponse<Void>> createOrUpdateCart(@PathVariable Long productId, @Valid @RequestBody CartRequest request){
        cartService.addCreateOrUpdateCart(productId,request);
        return ResponseEntity.ok(ApiResponse.success("Item added to cart successfully"));
    }

    @Operation(
            summary = "View cart",
            description = "Retrieves the current cart for a given user, including items and total price.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Cart retrieved successfully",
                            content = @Content(schema = @Schema(implementation = Cart.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Cart not found for the user",
                            content = @Content
                    )
            }
    )
    @GetMapping
    @PreAuthorize("hasAuthority(T(com.ecommerce.user.enums.PermissionType).VIEW_CART.name())")
    public ResponseEntity<ApiResponse<CartResponse>> viewCart(@RequestParam(name = "user_Id") Long userId){
        return ResponseEntity.ok(ApiResponse.success(cartService.getCartByUserId(userId)));
    }
}

package com.ecommerce.cart.controller;

import com.ecommerce.cart.dto.request.CartRequest;
import com.ecommerce.cart.dto.request.UpdateCartItemRequest;
import com.ecommerce.cart.dto.response.CartResponse;
import com.ecommerce.cart.services.CartService;
import com.ecommerce.common.constants.ApiEndPoints;
import com.ecommerce.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiEndPoints.Cart.BASE_CART)
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Tag(
        name = "Cart",
        description = "Operation for managing user shopping carts"
)
public class CartController {
    private final CartService cartService;

    @Operation(
            summary = "Get user cart",
            description = "Cart details by user id",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Fetch cart details.",
                            content = @Content(
                                    schema = @Schema(implementation = CartResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Cart not found.",
                            content = @Content
                    )
            }
    )
    @PreAuthorize("hasAuthority(T(com.ecommerce.user.enums.PermissionType).VIEW_CART.name())")
    @GetMapping(ApiEndPoints.Cart.VIEW_CART)
    public ResponseEntity<ApiResponse<CartResponse>> getCart(@PathVariable Long userId){
        return ResponseEntity.ok(ApiResponse.success(cartService.getCart(userId)));
    }

    @Operation(
            summary = "Create cart",
            description = "Add a product to user's cart",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "201",
                            description = "Product add to cart successfully.",
                            content = @Content(
                                    schema = @Schema(implementation = CartResponse.class)
                            )
                    )
            }
    )
    @PreAuthorize("hasAuthority(T(com.ecommerce.user.enums.PermissionType).ADD_TO_CART.name())")
    @PostMapping(ApiEndPoints.Cart.ADD_TO_CART)
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(@Valid @RequestBody CartRequest request){
        return ResponseEntity.ok(ApiResponse.success("Add product to cart",cartService.addToCart(request)));
    }

    @PreAuthorize("hasAuthority(T(com.ecommerce.user.enums.PermissionType).UPDATE_CART.name())")
    @PutMapping(ApiEndPoints.Cart.UPDATE_CART)
    public ResponseEntity<ApiResponse<CartResponse>> updateCart(@PathVariable Long cartItemId, @Valid @RequestBody UpdateCartItemRequest request){
        return ResponseEntity.ok(ApiResponse.success("Cart updated successfully",cartService.updateCart(cartItemId,request)));
    }

//    @PreAuthorize("hasAuthority(T(com.ecommerce.user.enums.PermissionType).DELETE_CART.name())")
//    @DeleteMapping(ApiEndPoints.Cart.DELETE_CART)
//    public ResponseEntity<ApiResponse<CartResponse>> deleteCart(@PathVariable Long cartItemId){
//        cartService.deleteCart(cartItemId);
//        return ResponseEntity.ok(ApiResponse.success("Cart deleted successfully",null));
//    }
//
//    @PreAuthorize("hasAuthority(T(com.ecommerce.user.enums.PermissionType).CLEAR_CART.name())")
//    @DeleteMapping("/clear")
//    public ResponseEntity<ApiResponse<Void>> clearCart(){
//        cartService.clearCart();
//        return ResponseEntity.ok(ApiResponse.success("Cart Crear successfully."));
//    }

}

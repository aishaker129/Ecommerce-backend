package com.ecommerce.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    SUPER_ADMIN("Super Admin"),
    ADMIN("Admin"),
    SELLER("Seller"),
    CUSTOMER("Customer");
    private final String displayValue;
}

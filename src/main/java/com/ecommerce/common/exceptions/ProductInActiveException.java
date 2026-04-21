package com.ecommerce.common.exceptions;

public class ProductInActiveException extends RuntimeException{
    public ProductInActiveException(String message){
        super(message);
    }
}

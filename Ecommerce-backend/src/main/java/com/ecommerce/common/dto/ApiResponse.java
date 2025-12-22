package com.ecommerce.common.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T>{
    private Boolean success;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<T>(true, "Operation successfull", data);
    }

    public static <T> ApiResponse<T> success(String message, T data){
        return new ApiResponse<T>(true, message, data);
    }
}

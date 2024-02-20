package com.shop.online.demo.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ApiValidationError extends ApiSubError {
    private String fieldName;
    private String message;
}

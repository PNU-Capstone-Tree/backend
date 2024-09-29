package com.tree.tree.config.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    private String message;

    public static ErrorResponse from(final String message) {
        return new ErrorResponse(message);
    }
}

package com.internship.apigateway.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FallbackDto {
    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();
    public FallbackDto(String message) {
        this.message = message;
    }
}

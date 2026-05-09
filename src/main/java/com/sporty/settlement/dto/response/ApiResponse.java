package com.sporty.settlement.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public record ApiResponse<T>(String system, @JsonFormat(shape = JsonFormat.Shape.STRING) Instant timestamp, T data) {

    public static <T> ApiResponse<T> of(T data) {
        return new ApiResponse<>("settlement-service", Instant.now(), data);
    }
}

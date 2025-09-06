package com.retailpulse.dto.response;

public record BusinessEntityResponseDto(Long id, String name, String location, String type, Boolean external, Boolean active) {
}
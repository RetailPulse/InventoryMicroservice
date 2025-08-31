package com.retailpulse.dto.response;

public record InventoryResponseDto(Long id, Long productId, Long businessEntityId, int quantity,
                                   double totalCostPrice) {
}


package com.retailpulse.controller.response;

public record InventoryResponseDto(Long id, Long productId, Long businessEntityId, int quantity,
                                   double totalCostPrice) {
}


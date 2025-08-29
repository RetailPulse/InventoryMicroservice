package com.retailpulse.controller.response;

import java.time.Instant;
import java.util.UUID;

public record InventoryTransactionResponseDto(UUID id, Long productId, int quantity, double costPricePerUnit,
                                              Long source, Long destination, Instant insertedAt) {
}
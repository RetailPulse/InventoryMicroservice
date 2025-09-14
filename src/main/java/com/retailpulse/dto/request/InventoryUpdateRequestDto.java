package com.retailpulse.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record InventoryUpdateRequestDto(
    @Min(1) long businessEntityId,

    @NotEmpty
    List<InventoryItem> items
) {
    public record InventoryItem(
        @Min(1) long productId,
        int quantity
    ) {}
}

package com.retailpulse.controller.response;

import com.retailpulse.entity.InventoryTransaction;
import com.retailpulse.entity.Product;

public record InventoryTransactionProductResponseDto(InventoryTransaction inventoryTransaction, Product product) {
}

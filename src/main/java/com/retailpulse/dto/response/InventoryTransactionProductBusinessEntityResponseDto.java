package com.retailpulse.dto.response;

import com.retailpulse.entity.InventoryTransaction;
import com.retailpulse.entity.Product;

public record InventoryTransactionProductBusinessEntityResponseDto (InventoryTransaction inventoryTransaction,
                                                                    Product product,
                                                                    BusinessEntityResponseDto source,
                                                                    BusinessEntityResponseDto destination) {
}

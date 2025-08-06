package com.retailpulse.DTO;

import com.retailpulse.entity.InventoryTransaction;
import com.retailpulse.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryTransactionProductDto {
    private InventoryTransaction inventoryTransaction;
    private Product product;
}
package com.retailpulse.service;

import com.retailpulse.entity.Inventory;
import com.retailpulse.entity.SalesDetails;
import com.retailpulse.entity.SalesTransaction;
import com.retailpulse.exception.ErrorCodes;
import com.retailpulse.repository.InventoryRepository;
import com.retailpulse.service.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class StockUpdateService {

    private final InventoryRepository inventoryRepository;

    public StockUpdateService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public void addStock(SalesTransaction salesTransaction) {
        for (SalesDetails salesDetails : salesTransaction.getSalesDetailEntities()) {
            Inventory inventory = inventoryRepository.findByProductIdAndBusinessEntityId(salesDetails.getProductId(), salesTransaction.getBusinessEntityId())
                    .orElseThrow(() -> new BusinessException(ErrorCodes.NOT_FOUND, "Inventory not found for product id: " + salesDetails.getProductId()));

            inventory.setQuantity(inventory.getQuantity() + salesDetails.getQuantity());
            inventoryRepository.save(inventory);
        }
    }

    public void deductStock(SalesTransaction salesTransaction) {
        for (SalesDetails salesDetails : salesTransaction.getSalesDetailEntities()) {
            Inventory inventory = inventoryRepository.findByProductIdAndBusinessEntityId(salesDetails.getProductId(), salesTransaction.getBusinessEntityId())
                    .orElseThrow(() -> new BusinessException(ErrorCodes.NOT_FOUND, "Inventory not found for product id: " + salesDetails.getProductId()));

            if (inventory.getQuantity() < salesDetails.getQuantity()) {
                throw new BusinessException(ErrorCodes.INSUFFICIENT_INVENTORY, "Insufficient stock for product id: " + salesDetails.getProductId());
            }

            inventory.setQuantity(inventory.getQuantity() - salesDetails.getQuantity());
            inventoryRepository.save(inventory);

        }
    }

}

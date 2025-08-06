package com.retailpulse.service;

import com.retailpulse.entity.Inventory;
import com.retailpulse.repository.InventoryRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public Optional<Inventory> getInventoryById(Long id) {
        return inventoryRepository.findById(id);
    }

    public List<Inventory> getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId);
    }

    public List<Inventory> getInventoryByBusinessEntityId(Long businessEntityId) {
        return inventoryRepository.findByBusinessEntityId(businessEntityId);
    }

    public Optional<Inventory> getInventoryByProductIdAndBusinessEntityId(Long productId, Long businessEntityId) {
        return inventoryRepository.findByProductIdAndBusinessEntityId(productId, businessEntityId);
    }

    public boolean inventoryContainsProduct(Long productId) {
        List<Inventory> inventoryList = getInventoryByProductId(productId);
        return !inventoryList.isEmpty();
    }
    // Not exposed in controller - Inventory should only be changed by Inventory Summary
    public Inventory saveInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    // Not exposed in controller - Inventory should only be changed by Inventory Summary
    public Inventory updateInventory(Long id, @NotNull Inventory inventoryDetails) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));

        // Update fields from the incoming details if provided
        updateField(inventoryDetails.getProductId(), inventory::setProductId);
        updateField(inventoryDetails.getBusinessEntityId(), inventory::setBusinessEntityId);

        if (inventoryDetails.getQuantity() >= 0) {
            updateField(inventoryDetails.getQuantity(), inventory::setQuantity);
        }

        if (inventoryDetails.getTotalCostPrice() >= 0) {
            updateField(inventoryDetails.getTotalCostPrice(), inventory::setTotalCostPrice);
        }
        return inventoryRepository.save(inventory);
    }

    // Generic helper method for updating fields
    private <T> void updateField(T newValue, Consumer<T> updater) {
        if(newValue == null) {
            return;
        }
        if (newValue instanceof String && ((String) newValue).isEmpty()) {
            return;
        }
        updater.accept(newValue);
    }

    // Not exposed in controller - Inventory should only be changed by Inventory Summary
    public Inventory deleteInventory(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));

        inventoryRepository.delete(inventory);
        return inventory;
    }
}

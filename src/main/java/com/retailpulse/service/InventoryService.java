package com.retailpulse.service;

import com.retailpulse.dto.response.InventoryResponseDto;
import com.retailpulse.entity.Inventory;
import com.retailpulse.repository.InventoryRepository;
import com.retailpulse.service.exception.BusinessException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class InventoryService {
    private static final String INVENTORY_NOT_FOUND = "INVENTORY_NOT_FOUND";
    private static final String INVENTORY_BY_PRODUCT_AND_BUSINESS_ENTITY_NOT_FOUND = "INVENTORY_BY_PRODUCT_AND_BUSINESS_ENTITY_NOT_FOUND";
    private static final String INVENTORY_NOT_FOUND_DESC = "Inventory not found with id: ";
    private static final String INVENTORY_BY_PRODUCT_AND_BUSINESS_ENTITY_NOT_FOUND_DESC = "Inventory by Product and Business Entity not found (ProductId, Business Entity): ";

    private static final String INVALID_BUSINESS_ENTITY = "INVALID_BUSINESS_ENTITY";
    private static final String INVALID_BUSINESS_ENTITY_DESC = "Not a valid business entity: ";

    private final InventoryRepository inventoryRepository;
    private final BusinessEntityService businessEntityService;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository, BusinessEntityService businessEntityService) {
        this.inventoryRepository = inventoryRepository;
        this.businessEntityService = businessEntityService;
    }

    public List<InventoryResponseDto> getAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(inventoryEntity -> new InventoryResponseDto(
                        inventoryEntity.getId(),
                        inventoryEntity.getProductId(),
                        inventoryEntity.getBusinessEntityId(),
                        inventoryEntity.getQuantity(),
                        inventoryEntity.getTotalCostPrice()
                ))
                .toList();
    }

    public InventoryResponseDto getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(INVENTORY_NOT_FOUND, INVENTORY_NOT_FOUND_DESC + id));

        return new InventoryResponseDto(
                inventory.getId(),
                inventory.getProductId(),
                inventory.getBusinessEntityId(),
                inventory.getQuantity(),
                inventory.getTotalCostPrice()
        );
    }

    public List<InventoryResponseDto> getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId).stream()
                .map(inventoryEntity -> new InventoryResponseDto(
                        inventoryEntity.getId(),
                        inventoryEntity.getProductId(),
                        inventoryEntity.getBusinessEntityId(),
                        inventoryEntity.getQuantity(),
                        inventoryEntity.getTotalCostPrice()
                ))
                .toList();
    }

    public List<InventoryResponseDto> getInventoryByBusinessEntityId(Long businessEntityId) {
        if (!businessEntityService.isValidBusinessEntity(businessEntityId)) {
            throw new BusinessException(INVALID_BUSINESS_ENTITY, INVALID_BUSINESS_ENTITY_DESC + businessEntityId);
        }

        return inventoryRepository.findByBusinessEntityId(businessEntityId).stream()
                .map(inventoryEntity -> new InventoryResponseDto(
                        inventoryEntity.getId(),
                        inventoryEntity.getProductId(),
                        inventoryEntity.getBusinessEntityId(),
                        inventoryEntity.getQuantity(),
                        inventoryEntity.getTotalCostPrice()
                ))
                .toList();

    }

     public InventoryResponseDto getInventoryByProductIdAndBusinessEntityId(Long productId, Long businessEntityId) {
        if (!businessEntityService.isValidBusinessEntity(businessEntityId)) {
            throw new BusinessException(INVALID_BUSINESS_ENTITY, INVALID_BUSINESS_ENTITY_DESC + businessEntityId);
        }

        Inventory inventory = inventoryRepository.findByProductIdAndBusinessEntityId(productId, businessEntityId)
                .orElseThrow(() -> new BusinessException(INVENTORY_BY_PRODUCT_AND_BUSINESS_ENTITY_NOT_FOUND,
                        INVENTORY_BY_PRODUCT_AND_BUSINESS_ENTITY_NOT_FOUND_DESC + "(" + productId + ", " + businessEntityId + ")"));

        return new InventoryResponseDto(
                inventory.getId(),
                inventory.getProductId(),
                inventory.getBusinessEntityId(),
                inventory.getQuantity(),
                inventory.getTotalCostPrice()
        );
    }

    public boolean inventoryContainsProduct(Long productId) {
        List<InventoryResponseDto> inventoryList = getInventoryByProductId(productId);
        return !inventoryList.isEmpty();
    }

    // Not exposed in controller - Inventory should only be changed by Inventory Summary
    public Inventory saveInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    // Not exposed in controller - Inventory should only be changed by Inventory Summary
    public Inventory updateInventory(Long id, @NotNull Inventory inventoryDetails) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(INVENTORY_NOT_FOUND, INVENTORY_NOT_FOUND_DESC + id));

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
        if (newValue == null) {
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
                .orElseThrow(() -> new BusinessException(INVENTORY_NOT_FOUND, INVENTORY_NOT_FOUND_DESC + id));

        inventoryRepository.delete(inventory);
        return inventory;
    }
}
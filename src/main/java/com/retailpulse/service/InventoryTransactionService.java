package com.retailpulse.service;

import com.retailpulse.client.BusinessEntityService;
import com.retailpulse.controller.response.InventoryResponseDto;
import com.retailpulse.controller.response.InventoryTransactionProductResponseDto;
import com.retailpulse.controller.response.InventoryTransactionResponseDto;
import com.retailpulse.controller.response.ProductResponseDto;
import com.retailpulse.entity.Inventory;
import com.retailpulse.entity.InventoryTransaction;
import com.retailpulse.repository.InventoryTransactionRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Service
public class InventoryTransactionService {

    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final InventoryService inventoryService;
    private final ProductService productService;
    private final BusinessEntityService businessEntityService;

    @Autowired
    public InventoryTransactionService(InventoryTransactionRepository inventoryTransactionRepository,
                                       InventoryService inventoryService,
                                       ProductService productService,
                                       BusinessEntityService businessEntityService) {
        this.inventoryTransactionRepository = inventoryTransactionRepository;
        this.inventoryService = inventoryService;
        this.productService = productService;
        this.businessEntityService = businessEntityService;
    }

    public List<InventoryTransactionProductResponseDto> getAllInventoryTransactionWithProduct() {
        return inventoryTransactionRepository.findAllWithProduct().stream()
                .map(InventoryTransactionProduct -> new InventoryTransactionProductResponseDto(
                        InventoryTransactionProduct.inventoryTransaction(),
                        InventoryTransactionProduct.product()
                ))
                .toList();
    }

    public InventoryTransactionResponseDto saveInventoryTransaction(@NotNull InventoryTransaction inventoryTransaction) {
        validateInventoryTransactionRequestBody(inventoryTransaction);

        long productId = inventoryTransaction.getProductId();
        long sourceId = inventoryTransaction.getSource();
        long destinationId = inventoryTransaction.getDestination();
        int quantity = inventoryTransaction.getQuantity();
        double costPricePerUnit = inventoryTransaction.getCostPricePerUnit();

        final boolean isSourceExternal;
        try {
            isSourceExternal = this.businessEntityService.isExternalBusinessEntity(sourceId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to retrieve source business entity with id: " + sourceId, e);
        }

        // Source External: No need to validate/deduct source inventory
        if (!isSourceExternal) {
            // Validate source inventory
            InventoryResponseDto sourceInventory = inventoryService.getInventoryByProductIdAndBusinessEntityId(productId, sourceId);
            if (sourceInventory == null) {
                throw new IllegalArgumentException("Source inventory not found for product id: "
                        + productId + " and source id: " + sourceId);
            }
            if (sourceInventory.quantity() < quantity) {
                throw new IllegalArgumentException("Not enough quantity in source inventory for product id: "
                        + productId + " and source id: " + sourceId + ". Available: "
                        + sourceInventory.quantity() + ", required: " + quantity);
            }
            // Update source inventory: deduct the quantity
            Inventory existingSourceInventory = new Inventory();
            existingSourceInventory.setId(sourceInventory.id());
            existingSourceInventory.setProductId(sourceInventory.productId());
            existingSourceInventory.setBusinessEntityId(sourceInventory.businessEntityId());
            existingSourceInventory.setQuantity(sourceInventory.quantity() - quantity);
            existingSourceInventory.setTotalCostPrice(sourceInventory.totalCostPrice() - (costPricePerUnit * quantity));
            inventoryService.updateInventory(existingSourceInventory.getId(), existingSourceInventory);
        }

        final boolean isDestinationExternal;
        try {
            isDestinationExternal = this.businessEntityService.isExternalBusinessEntity(destinationId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to retrieve destination business entity with id: " + destinationId, e);
        }

        // Destination External: No need to deduct destination inventory
        if (!isDestinationExternal) {
            // Update or create destination inventory
            InventoryResponseDto destinationInventory;
            try {
                // If the underlying service throws when inventory not found, treat it as "not found"
                destinationInventory = inventoryService.getInventoryByProductIdAndBusinessEntityId(productId, destinationId);
            } catch (Exception e) {
                destinationInventory = null;
            }

            if (destinationInventory == null) {
                // Create new inventory for destination since it does not exist.
                Inventory newDestinationInventory = new Inventory();
                newDestinationInventory.setProductId(productId);
                newDestinationInventory.setBusinessEntityId(destinationId);
                newDestinationInventory.setQuantity(quantity);
                newDestinationInventory.setTotalCostPrice(costPricePerUnit * quantity);
                inventoryService.saveInventory(newDestinationInventory);
            } else {
                // Update existing destination inventory by adding the quantity.
                Inventory existingDestinationInventory = new Inventory();
                existingDestinationInventory.setId(destinationInventory.id());
                existingDestinationInventory.setProductId(destinationInventory.productId());
                existingDestinationInventory.setBusinessEntityId(destinationInventory.businessEntityId());
                existingDestinationInventory.setQuantity(destinationInventory.quantity() + quantity);
                existingDestinationInventory.setTotalCostPrice(destinationInventory.totalCostPrice() + (costPricePerUnit * quantity));
                inventoryService.updateInventory(existingDestinationInventory.getId(), existingDestinationInventory);
            }
        }

        // Proceed with saving the transaction
        InventoryTransaction createdinventoryTransaction = inventoryTransactionRepository.save(inventoryTransaction);
        return new InventoryTransactionResponseDto(
                createdinventoryTransaction.getId(),
                createdinventoryTransaction.getProductId(),
                createdinventoryTransaction.getQuantity(),
                createdinventoryTransaction.getCostPricePerUnit(),
                createdinventoryTransaction.getSource(),
                createdinventoryTransaction.getDestination(),
                createdinventoryTransaction.getInsertedAt()
        );
    }
    /* Some of the things to consider when creating Update/Delete method
     * Update -
     * If Product can be updated
     * * Need to add original Product to source inventory
     * If source can be updated
     * * Need to add Product back to original source inventory
     * If destination can be updated
     * * Need to minus Product from original destination inventory
     * If quantity can be updated
     * * If source same then need see the difference; if positive or negative;
     *
     *
     * Example Note:
     *
     * 1. If Original ProductId = Updated ProductId
     * * 1.1 Original Source = Updated Source
     * * * 1.1.1 If quantity > updatedQuantity -> Need to add back to source
     * * * 1.1.2 If quantity < updatedQuantity -> Need to minus
     * * 1.2 Original Source != Updated Source
     * * * 1.2.1 Add back inventory to original source; Minus inventory from updated source
     *
     * 2. If Original ProductId != Updated ProductId
     * * 2.1 Original Source = Updated Source
     * * * 2.1.1 Add back inventory of original ProductId to source
     * * * 2.1.2 If updatedQuantity
     */

    // todo - To plan how to implement this logic in Inventory
//    private boolean isExternalBusinessEntity(Long id) {
//        BusinessEntity businessEntity = businessEntityRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Business Entity not found with id: " + id));
//        return businessEntity.isExternal();
//    }


    // Helper Method
    public InventoryTransactionResponseDto updateInventoryTransaction(UUID id, InventoryTransaction inventoryTransactionDetails) {
        InventoryTransaction inventoryTransaction = inventoryTransactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));

        updateField(inventoryTransactionDetails.getProductId(), inventoryTransaction::setProductId);
        updateField(inventoryTransactionDetails.getQuantity(), inventoryTransaction::setQuantity);
        updateField(inventoryTransactionDetails.getCostPricePerUnit(), inventoryTransaction::setCostPricePerUnit);
        updateField(inventoryTransactionDetails.getSource(), inventoryTransaction::setSource);
        updateField(inventoryTransactionDetails.getDestination(), inventoryTransaction::setDestination);
        InventoryTransaction updatedInventoryTransaction = inventoryTransactionRepository.save(inventoryTransaction);
        return new InventoryTransactionResponseDto(
                updatedInventoryTransaction.getId(),
                updatedInventoryTransaction.getProductId(),
                updatedInventoryTransaction.getQuantity(),
                updatedInventoryTransaction.getCostPricePerUnit(),
                updatedInventoryTransaction.getSource(),
                updatedInventoryTransaction.getDestination(),
                updatedInventoryTransaction.getInsertedAt()
        );
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

    // Validation Method
    private void validateInventoryTransactionRequestBody(@NotNull InventoryTransaction inventoryTransaction) {
        long productId = inventoryTransaction.getProductId();
        long sourceId = inventoryTransaction.getSource();
        long destinationId = inventoryTransaction.getDestination();

        // Validate input Product
        ProductResponseDto product = productService.getProductById(productId);
        if (!product.active()) {
            throw new IllegalArgumentException("Product deleted for product id: " + productId);
        }
        // Validate input source & destination
        if (sourceId == destinationId) {
            throw new IllegalArgumentException("Source and Destination cannot be the same");
        }
        // Validate input quantity
        if (inventoryTransaction.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity cannot be negative or zero");
        }
        // Validate input cost price per unit
        if (inventoryTransaction.getCostPricePerUnit() < 0) {
            throw new IllegalArgumentException("Cost price per unit cannot be negative");
        }
    }
}
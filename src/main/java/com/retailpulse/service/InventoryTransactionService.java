package com.retailpulse.service;

import com.retailpulse.DTO.InventoryTransactionProductDto;
import com.retailpulse.entity.BusinessEntity;
import com.retailpulse.entity.Inventory;
import com.retailpulse.entity.InventoryTransaction;
import com.retailpulse.entity.Product;
import com.retailpulse.repository.BusinessEntityRepository;
import com.retailpulse.repository.InventoryTransactionRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Service
public class InventoryTransactionService {

    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final InventoryService inventoryService;
    private final ProductService productService;
//    private final BusinessEntityRepository businessEntityRepository;

    @Autowired
    public InventoryTransactionService(InventoryTransactionRepository inventoryTransactionRepository,
                                       InventoryService inventoryService,
                                       ProductService productService,
                                       BusinessEntityRepository businessEntityRepository) {
        this.inventoryTransactionRepository = inventoryTransactionRepository;
        this.inventoryService = inventoryService;
        this.productService = productService;
        this.businessEntityRepository = businessEntityRepository;
    }

    public List<InventoryTransactionProductDto> getAllInventoryTransactionWithProduct() {
        return inventoryTransactionRepository.findAllWithProduct();
    }

    public InventoryTransaction saveInventoryTransaction(@NotNull InventoryTransaction inventoryTransaction) {
        validateInventoryTransactionRequestBody(inventoryTransaction);

        long productId = inventoryTransaction.getProductId();
        long sourceId = inventoryTransaction.getSource();
        long destinationId = inventoryTransaction.getDestination();
        int quantity = inventoryTransaction.getQuantity();
        double costPricePerUnit = inventoryTransaction.getCostPricePerUnit();

        boolean isSourceExternal = this.isExternalBusinessEntity(sourceId);
        // Source External: No need to validate/deduct source inventory
        if (!isSourceExternal) {
            // Validate source inventory
            Optional<Inventory> sourceInventory = inventoryService.getInventoryByProductIdAndBusinessEntityId(productId, sourceId);
            if (sourceInventory.isEmpty()) {
                throw new IllegalArgumentException("Source inventory not found for product id: "
                        + productId + " and source id: " + sourceId);
            }
            if (sourceInventory.get().getQuantity() < quantity) {
                throw new IllegalArgumentException("Not enough quantity in source inventory for product id: "
                        + productId + " and source id: " + sourceId + ". Available: "
                        + sourceInventory.get().getQuantity() + ", required: " + quantity);
            }

            // Update source inventory: deduct the quantity
            Inventory existingSourceInventory = sourceInventory.get();
            existingSourceInventory.setQuantity(existingSourceInventory.getQuantity() - quantity);
            existingSourceInventory.setTotalCostPrice(existingSourceInventory.getTotalCostPrice() - (costPricePerUnit * quantity));
            inventoryService.updateInventory(existingSourceInventory.getId(), existingSourceInventory);
        }

        boolean isDestinationExternal = this.isExternalBusinessEntity(destinationId);
        // Destination External: No need to deduct destination inventory
        if (!isDestinationExternal) {
            // Update or create destination inventory
            Optional<Inventory> destinationInventory = inventoryService.getInventoryByProductIdAndBusinessEntityId(productId, destinationId);
            if (destinationInventory.isEmpty()) {
                // Create new inventory for destination since it does not exist.
                Inventory newDestinationInventory = new Inventory();
                newDestinationInventory.setProductId(productId);
                newDestinationInventory.setBusinessEntityId(destinationId);
                newDestinationInventory.setQuantity(quantity);
                newDestinationInventory.setTotalCostPrice(costPricePerUnit * quantity);
                inventoryService.saveInventory(newDestinationInventory);
            } else {
                // Update existing destination inventory by adding the quantity.
                Inventory existingDestinationInventory = destinationInventory.get();
                existingDestinationInventory.setQuantity(existingDestinationInventory.getQuantity() + quantity);
                existingDestinationInventory.setTotalCostPrice(existingDestinationInventory.getTotalCostPrice() + (costPricePerUnit * quantity));
                inventoryService.updateInventory(existingDestinationInventory.getId(), existingDestinationInventory);
            }
        }

        // Proceed with saving the transaction
        return inventoryTransactionRepository.save(inventoryTransaction);
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
    private InventoryTransaction updateInventoryTransaction(UUID id, InventoryTransaction inventoryTransactionDetails) {
        InventoryTransaction inventoryTransaction = inventoryTransactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));

        updateField(inventoryTransactionDetails.getProductId(), inventoryTransaction::setProductId);
        updateField(inventoryTransactionDetails.getQuantity(), inventoryTransaction::setQuantity);
        updateField(inventoryTransactionDetails.getCostPricePerUnit(), inventoryTransaction::setCostPricePerUnit);
        updateField(inventoryTransactionDetails.getSource(), inventoryTransaction::setSource);
        updateField(inventoryTransactionDetails.getDestination(), inventoryTransaction::setDestination);
        return inventoryTransactionRepository.save(inventoryTransaction);
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
        Optional<Product> product = productService.getProductById(productId);
        if (product.isEmpty()) {
            throw new IllegalArgumentException("Product not found for product id: " + productId);
        }
        if (!product.get().isActive()) {
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
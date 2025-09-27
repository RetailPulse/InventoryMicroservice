package com.retailpulse.controller;

import com.retailpulse.dto.request.TimeSearchFilterRequestDto;
import com.retailpulse.dto.response.InventoryTransactionProductBusinessEntityResponseDto;
import com.retailpulse.dto.response.InventoryTransactionProductResponseDto;
import com.retailpulse.dto.response.InventoryTransactionResponseDto;
import com.retailpulse.entity.InventoryTransaction;
import com.retailpulse.service.InventoryTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/inventoryTransaction")
public class InventoryTransactionController {
    private static final Logger logger = Logger.getLogger(InventoryTransactionController.class.getName());
    private final InventoryTransactionService inventoryTransactionService;

    @Autowired
    public InventoryTransactionController(InventoryTransactionService inventoryTransactionService) {
        this.inventoryTransactionService = inventoryTransactionService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryTransactionProductResponseDto>> getAllInventoryTransactionWithProduct() {
        logger.info("Fetching all inventory transactions");
        return ResponseEntity.ok(inventoryTransactionService.getAllInventoryTransactionWithProduct());
    }

    @PostMapping("/withBusinessEntityDetails")
    public ResponseEntity<List<InventoryTransactionProductBusinessEntityResponseDto>> getAllInventoryTransactionWithProductAndBusinessEntity(@RequestBody TimeSearchFilterRequestDto searchFilters) {
        logger.info("Fetching all inventory transactions with business entity details");
        return ResponseEntity.ok(inventoryTransactionService.getAllInventoryTransactionWithProductAndBusinessEntity(searchFilters));
    }

    @PostMapping
    public ResponseEntity<InventoryTransactionResponseDto> createInventoryTransaction(@RequestBody InventoryTransaction inventoryTransaction) {
        // inventoryTransaction.getQuantity() will always be positive
        logger.info("Received request to create inventoryTransaction: " + inventoryTransaction);
        try {
            InventoryTransactionResponseDto createdInventoryTransaction = inventoryTransactionService.saveInventoryTransaction(inventoryTransaction);
            logger.info("Successfully created createdInventoryTransaction: " + createdInventoryTransaction);
            return ResponseEntity.ok(createdInventoryTransaction);
        } catch (Exception e) {
            logger.severe("Error creating createdInventoryTransaction: " + e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    public InventoryTransactionResponseDto updateInventoryTransaction(@PathVariable UUID id, @RequestBody InventoryTransaction inventoryTransaction) {
        logger.info("Received request to update inventory transaction with id: " + id);
        try {
            InventoryTransactionResponseDto updatedInventoryTransaction = inventoryTransactionService.updateInventoryTransaction(id, inventoryTransaction);
            logger.info("Successfully updated inventory transaction with id: " + updatedInventoryTransaction.id());
            return updatedInventoryTransaction;
        } catch (Exception e) {
            logger.severe("Error updating inventory transaction: " + e.getMessage());
            throw e;
        }
    }
}

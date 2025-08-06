package com.retailpulse.controller;

import com.retailpulse.entity.Inventory;
import com.retailpulse.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private static final Logger logger = Logger.getLogger(InventoryController.class.getName());

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventories() {
        logger.info("Fetching all inventories");
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        logger.info("Fetching inventory with id: " + id);
        Inventory inventory = inventoryService.getInventoryById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Inventory not found with id: " + id));
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/productId/{id}")
    public ResponseEntity<List<Inventory>> getInventoryByProductId(@PathVariable Long id) {
        logger.info("Fetching inventory with productId: " + id);
        return ResponseEntity.ok(inventoryService.getInventoryByProductId(id));
    }

    @GetMapping("/businessEntityId/{id}")
    public ResponseEntity<List<Inventory>> getInventoryByBusinessEntityId(@PathVariable Long id) {
        logger.info("Fetching inventory with businessEntityId: " + id);
        return ResponseEntity.ok(inventoryService.getInventoryByBusinessEntityId(id));
    }

    @GetMapping("/productId/{productId}/businessEntityId/{businessEntityId}")
    public ResponseEntity<Inventory> getInventoryByProductIdAndBusinessEntityId(@PathVariable Long productId, @PathVariable Long businessEntityId) {
        logger.info("Fetching inventory with businessEntityId (" + businessEntityId + ") and productId (" + productId + ")");
        Inventory inventory = inventoryService.getInventoryByProductIdAndBusinessEntityId(productId, businessEntityId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Inventory not found with productId: " + productId + " and businessEntityId: " + businessEntityId));
        return ResponseEntity.ok(inventory);
    }
}

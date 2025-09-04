package com.retailpulse.service;

import com.retailpulse.client.BusinessEntityClient;
import com.retailpulse.dto.response.*;
import com.retailpulse.entity.Inventory;
import com.retailpulse.entity.InventoryTransaction;
import com.retailpulse.entity.Product;
import com.retailpulse.repository.InventoryTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class InventoryTransactionServiceTest {

    @Mock
    private InventoryTransactionRepository mockInventoryTransactionRepository;

    @Mock
    private InventoryService mockInventoryService;

    @Mock
    private ProductService mockProductService;

    @Mock
    private BusinessEntityClient mockBusinessEntityClient;

    @Mock
    private BusinessEntityService mockBusinessEntityService;

    @InjectMocks
    private InventoryTransactionService inventoryTransactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllInventoryTransactionWithProduct() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setDescription("Product A");

        InventoryTransaction inventoryTransaction = new InventoryTransaction();
        inventoryTransaction.setProductId(1L);

        InventoryTransactionProductResponseDto dto1 = new InventoryTransactionProductResponseDto(inventoryTransaction, product);

        List<InventoryTransactionProductResponseDto> mockDtos = Collections.singletonList(dto1);

        when(mockInventoryTransactionRepository.findAllWithProduct()).thenReturn(mockDtos);

        // Act
        List<InventoryTransactionProductResponseDto> result = inventoryTransactionService.getAllInventoryTransactionWithProduct();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Product A", result.get(0).product().getDescription());

        verify(mockInventoryTransactionRepository, times(1)).findAllWithProduct();
        verifyNoMoreInteractions(mockInventoryTransactionRepository);
    }

    @Test
    void testSaveInventoryTransaction_Successful() {
        // Arrange
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProductId(1L);
        transaction.setSource(101L);
        transaction.setDestination(201L);
        transaction.setQuantity(10);
        transaction.setCostPricePerUnit(5.0);

        ProductResponseDto product = new ProductResponseDto(1L, "LEV-M-001", "Levis men jeans", "Jeans", "Men", "Levis", "USA", "each", "LEV", "LEV-B-001", 100.00, true);

        InventoryResponseDto sourceInventory = new InventoryResponseDto(1L, 1L, 101L, 20, 100.0);
        InventoryResponseDto destinationInventory = new InventoryResponseDto(2L, 1L, 201L, 30, 150.0);
        
        Inventory updatedSourceInventory = new Inventory();
        updatedSourceInventory.setId(1L);
        updatedSourceInventory.setProductId(1L);
        updatedSourceInventory.setBusinessEntityId(101L);
        updatedSourceInventory.setQuantity(10);
        updatedSourceInventory.setTotalCostPrice(50.0);
        
        Inventory updatedDestinationInventory = new Inventory();
        updatedDestinationInventory.setId(2L);
        updatedDestinationInventory.setProductId(1L);
        updatedDestinationInventory.setBusinessEntityId(201L);
        updatedDestinationInventory.setQuantity(40);
        updatedDestinationInventory.setTotalCostPrice(200.0);

        BusinessEntityResponseDto businessEntity = new BusinessEntityResponseDto(1L, "Waterway Point", "Punggol", "Shop", false,true);

        when(mockProductService.getProductById(1L)).thenReturn(product);
        when(mockBusinessEntityClient.getBusinessEntity(101L)).thenReturn(businessEntity);
        when(mockBusinessEntityClient.getBusinessEntity(201L)).thenReturn(businessEntity);
        when(mockInventoryService.getInventoryByProductIdAndBusinessEntityId(1L, 101L)).thenReturn(sourceInventory);
        when(mockInventoryService.getInventoryByProductIdAndBusinessEntityId(1L, 201L)).thenReturn(destinationInventory);
        when(mockInventoryService.updateInventory(1L, updatedSourceInventory)).thenReturn(updatedSourceInventory);
        when(mockInventoryService.updateInventory(2L, updatedDestinationInventory)).thenReturn(updatedDestinationInventory);
        when(mockInventoryTransactionRepository.save(transaction)).thenReturn(transaction);

        // Act
        InventoryTransactionResponseDto result = inventoryTransactionService.saveInventoryTransaction(transaction);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.productId());
        assertEquals(101L, result.source());
        assertEquals(201L, result.destination());
        assertEquals(10, result.quantity());
        assertEquals(5.0, result.costPricePerUnit(), 0.01);

        verify(mockProductService, times(1)).getProductById(1L);
        verify(mockInventoryService, times(1)).getInventoryByProductIdAndBusinessEntityId(1L, 101L);
        verify(mockInventoryService, times(1)).getInventoryByProductIdAndBusinessEntityId(1L, 201L);
        verify(mockInventoryService, times(1)).updateInventory(1L, updatedSourceInventory);
        verify(mockInventoryService, times(1)).updateInventory(2L, updatedDestinationInventory);
        verify(mockInventoryTransactionRepository, times(1)).save(transaction);
        verifyNoMoreInteractions(mockProductService, mockInventoryService, mockInventoryTransactionRepository);
    }

    @Test
    void testSaveInventoryTransaction_InsufficientSourceQuantity() {
        // Arrange
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProductId(1L);
        transaction.setSource(101L);
        transaction.setDestination(201L);
        transaction.setQuantity(30);
        transaction.setCostPricePerUnit(5.0);

        ProductResponseDto product = new ProductResponseDto(1L, "LEV-M-001", "Levis men jeans", "Jeans", "Men", "Levis", "USA", "each", "LEV", "LEV-B-001", 100.00, true);

        InventoryResponseDto sourceInventory = new InventoryResponseDto(1L, 1L, 101L, 20, 100.0);

        BusinessEntityResponseDto businessEntity = new BusinessEntityResponseDto(101L, "Waterway Point", "Punggol", "Shop", false,true);

        when(mockProductService.getProductById(1L)).thenReturn(product);
        when(mockBusinessEntityClient.getBusinessEntity(101L)).thenReturn(businessEntity);
        when(mockBusinessEntityService.isExternalBusinessEntity(101L)).thenReturn(false);
        when(mockInventoryService.getInventoryByProductIdAndBusinessEntityId(1L, 101L)).thenReturn(sourceInventory);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inventoryTransactionService.saveInventoryTransaction(transaction);
        });

        assertEquals(
                "Not enough quantity in source inventory for product id: 1 and source id: 101. Available: 20, required: 30",
                exception.getMessage()
        );

        verify(mockProductService, times(1)).getProductById(1L);
        verify(mockInventoryService, times(1)).getInventoryByProductIdAndBusinessEntityId(1L, 101L);
        verifyNoMoreInteractions(mockProductService, mockInventoryService);
    }

    @Test
    void testSaveInventoryTransaction_InvalidProduct() {
        // Arrange
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProductId(999L);
        transaction.setSource(101L);
        transaction.setDestination(201L);
        transaction.setQuantity(10);
        transaction.setCostPricePerUnit(5.0);

        Inventory updatedSourceInventory = new Inventory();
        updatedSourceInventory.setId(101L);
        updatedSourceInventory.setProductId(1L);
        updatedSourceInventory.setBusinessEntityId(101L);
        updatedSourceInventory.setQuantity(10);
        updatedSourceInventory.setTotalCostPrice(50.0);
        
        Inventory updatedDestinationInventory = new Inventory();
        updatedDestinationInventory.setId(201L);
        updatedDestinationInventory.setProductId(1L);
        updatedDestinationInventory.setBusinessEntityId(201L);
        updatedDestinationInventory.setQuantity(40);
        updatedDestinationInventory.setTotalCostPrice(200.0);

        when(mockProductService.getProductById(999L)).thenReturn(null);
        when(mockBusinessEntityService.isExternalBusinessEntity(101L)).thenReturn(false);
        when(mockInventoryService.updateInventory(101L, updatedSourceInventory)).thenReturn(updatedSourceInventory);
        when(mockInventoryService.updateInventory(201L, updatedDestinationInventory)).thenReturn(updatedDestinationInventory);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inventoryTransactionService.saveInventoryTransaction(transaction);
        });

        assertEquals("Product not found for product id: 999", exception.getMessage());

        verify(mockProductService, times(1)).getProductById(999L);
        verifyNoMoreInteractions(mockProductService);
    }

    @Test
    void testSaveInventoryTransaction_SourceSameAsDestination() {
        // Arrange
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProductId(1L);
        transaction.setSource(101L);
        transaction.setDestination(101L);
        transaction.setQuantity(10);
        transaction.setCostPricePerUnit(5.0);

        ProductResponseDto product = new ProductResponseDto(1L, "LEV-M-001", "Levis men jeans", "Jeans", "Men", "Levis", "USA", "each", "LEV", "LEV-B-001", 100.00, true);

        when(mockProductService.getProductById(1L)).thenReturn(product);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inventoryTransactionService.saveInventoryTransaction(transaction);
        });

        assertEquals("Source and Destination cannot be the same", exception.getMessage());

        verify(mockProductService, times(1)).getProductById(1L);
        verifyNoMoreInteractions(mockProductService);
    }

    @Test
    void testSaveInventoryTransaction_NegativeQuantity() {
        // Arrange
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProductId(1L);
        transaction.setSource(101L);
        transaction.setDestination(201L);
        transaction.setQuantity(-5);
        transaction.setCostPricePerUnit(5.0);

        ProductResponseDto product = new ProductResponseDto(1L, "LEV-M-001", "Levis men jeans", "Jeans", "Men", "Levis", "USA", "each", "LEV", "LEV-B-001", 100.00, true);

        when(mockProductService.getProductById(1L)).thenReturn(product);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inventoryTransactionService.saveInventoryTransaction(transaction);
        });

        assertEquals("Quantity cannot be negative or zero", exception.getMessage());

        verify(mockProductService, times(1)).getProductById(1L);
        verifyNoMoreInteractions(mockProductService);
    }

    @Test
    void testSaveInventoryTransaction_SourceExternal() {
        // Arrange
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProductId(1L);
        transaction.setSource(101L);
        transaction.setDestination(201L);
        transaction.setQuantity(10);
        transaction.setCostPricePerUnit(5.0);

        ProductResponseDto product = new ProductResponseDto(
                1L, "LEV-M-001", "Levis men jeans", "Jeans", "Men",
                "Levis", "USA", "each", "LEV", "LEV-B-001", 100.00, true
        );

        InventoryResponseDto destinationInventoryResponse =
                new InventoryResponseDto(2L, 1L, 201L, 50, 100.0);

        // Mock behaviors
        when(mockProductService.getProductById(1L)).thenReturn(product);
        when(mockBusinessEntityService.isExternalBusinessEntity(101L)).thenReturn(true);  // source = external
        when(mockBusinessEntityService.isExternalBusinessEntity(201L)).thenReturn(false); // destination = internal
        when(mockInventoryService.getInventoryByProductIdAndBusinessEntityId(1L, 201L))
                .thenReturn(destinationInventoryResponse);
        when(mockInventoryTransactionRepository.save(transaction)).thenReturn(transaction);

        // Act
        InventoryTransactionResponseDto result =
                inventoryTransactionService.saveInventoryTransaction(transaction);

        // Assert response
        assertNotNull(result);
        assertEquals(1L, result.productId());
        assertEquals(101L, result.source());
        assertEquals(201L, result.destination());
        assertEquals(10, result.quantity());
        assertEquals(5.0, result.costPricePerUnit(), 0.01);

        // Verify destination inventory update
        ArgumentCaptor<Inventory> captor = ArgumentCaptor.forClass(Inventory.class);
        verify(mockInventoryService, times(1)).updateInventory(eq(2L), captor.capture());

        Inventory updated = captor.getValue();
        assertEquals(2L, updated.getId());
        assertEquals(1L, updated.getProductId());
        assertEquals(201L, updated.getBusinessEntityId());
        assertEquals(60, updated.getQuantity());         // 50 + 10
        assertEquals(150.0, updated.getTotalCostPrice()); // 100.0 + (10 * 5.0)

        // Verify no source inventory calls (since source is external)
        verify(mockInventoryService, never())
                .getInventoryByProductIdAndBusinessEntityId(eq(1L), eq(101L));
        verify(mockInventoryService, never())
                .updateInventory(eq(1L), any(Inventory.class));

        // Verify transaction persisted
        verify(mockInventoryTransactionRepository, times(1)).save(transaction);
    }


    @Test
    void testSaveInventoryTransaction_DestinationExternal() {
        // Arrange
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProductId(1L);
        transaction.setSource(101L);
        transaction.setDestination(201L);
        transaction.setQuantity(10);
        transaction.setCostPricePerUnit(5.0);

        ProductResponseDto product = new ProductResponseDto(
                1L, "LEV-M-001", "Levis men jeans", "Jeans", "Men",
                "Levis", "USA", "each", "LEV", "LEV-B-001", 100.00, true
        );

        InventoryResponseDto sourceInventory =
                new InventoryResponseDto(1L, 1L, 101L, 20, 100.0);

        // Mock behaviors
        when(mockProductService.getProductById(1L)).thenReturn(product);
        when(mockBusinessEntityService.isExternalBusinessEntity(101L)).thenReturn(false); // source = internal
        when(mockBusinessEntityService.isExternalBusinessEntity(201L)).thenReturn(true);  // destination = external
        when(mockInventoryService.getInventoryByProductIdAndBusinessEntityId(1L, 101L))
                .thenReturn(sourceInventory);
        when(mockInventoryTransactionRepository.save(transaction)).thenReturn(transaction);

        // Act
        InventoryTransactionResponseDto result =
                inventoryTransactionService.saveInventoryTransaction(transaction);

        // Assert response
        assertNotNull(result);
        assertEquals(1L, result.productId());
        assertEquals(101L, result.source());
        assertEquals(201L, result.destination());
        assertEquals(10, result.quantity());
        assertEquals(5.0, result.costPricePerUnit(), 0.01);

        // Verify source inventory update
        ArgumentCaptor<Inventory> captor = ArgumentCaptor.forClass(Inventory.class);
        verify(mockInventoryService, times(1)).updateInventory(eq(1L), captor.capture());

        Inventory updated = captor.getValue();
        assertEquals(1L, updated.getId());
        assertEquals(1L, updated.getProductId());
        assertEquals(101L, updated.getBusinessEntityId());
        assertEquals(10, updated.getQuantity());        // 20 - 10
        assertEquals(50.0, updated.getTotalCostPrice()); // 100.0 - (10 * 5.0)

        // Verify no destination inventory updates
        verify(mockInventoryService, never())
                .updateInventory(eq(201L), any(Inventory.class));
        verify(mockInventoryService, never())
                .saveInventory(any(Inventory.class));

        // Verify transaction persisted
        verify(mockInventoryTransactionRepository, times(1)).save(transaction);
    }

    @Test
    void testSaveInventoryTransaction_BothExternal() {
        // Arrange
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProductId(1L);
        transaction.setSource(101L);
        transaction.setDestination(201L);
        transaction.setQuantity(10);
        transaction.setCostPricePerUnit(5.0);

        ProductResponseDto product = new ProductResponseDto(
                1L, "LEV-M-001", "Levis men jeans", "Jeans", "Men",
                "Levis", "USA", "each", "LEV", "LEV-B-001", 100.00, true
        );

        // Mock behaviors
        when(mockProductService.getProductById(1L)).thenReturn(product);
        when(mockBusinessEntityService.isExternalBusinessEntity(101L)).thenReturn(true);  // source = external
        when(mockBusinessEntityService.isExternalBusinessEntity(201L)).thenReturn(true);  // destination = external
        when(mockInventoryTransactionRepository.save(transaction)).thenReturn(transaction);

        // Act
        InventoryTransactionResponseDto result =
                inventoryTransactionService.saveInventoryTransaction(transaction);

        // Assert response
        assertNotNull(result);
        assertEquals(1L, result.productId());
        assertEquals(101L, result.source());
        assertEquals(201L, result.destination());
        assertEquals(10, result.quantity());
        assertEquals(5.0, result.costPricePerUnit(), 0.01);

        // Verify no inventory lookups or updates (both ends external)
        verify(mockInventoryService, never())
                .getInventoryByProductIdAndBusinessEntityId(anyLong(), anyLong());
        verify(mockInventoryService, never())
                .updateInventory(anyLong(), any(Inventory.class));

        // Transaction persisted
        verify(mockProductService, times(1)).getProductById(1L);
        verify(mockInventoryTransactionRepository, times(1)).save(transaction);
    }


    @Test
    void testSaveInventoryTransaction_NewDestinationInventory() {
        // Arrange
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProductId(1L);
        transaction.setSource(101L);
        transaction.setDestination(201L);
        transaction.setQuantity(10);
        transaction.setCostPricePerUnit(5.0);

        ProductResponseDto product = new ProductResponseDto(1L, "LEV-M-001", "Levis men jeans", "Jeans", "Men", "Levis", "USA", "each", "LEV", "LEV-B-001", 100.00, true);

        Inventory sourceInventory = new Inventory();
        sourceInventory.setId(1L);
        sourceInventory.setProductId(1L);
        sourceInventory.setBusinessEntityId(101L);
        sourceInventory.setQuantity(20);
        sourceInventory.setTotalCostPrice(100.0);

        InventoryResponseDto sourceInventoryResponse = new InventoryResponseDto(1L, 1L, 101L, 20, 100.0);

        Inventory updatedSourceInventory = new Inventory();
        updatedSourceInventory.setId(1L);
        updatedSourceInventory.setProductId(1L);
        updatedSourceInventory.setBusinessEntityId(101L);
        updatedSourceInventory.setQuantity(sourceInventory.getQuantity() - 10);
        updatedSourceInventory.setTotalCostPrice(sourceInventory.getTotalCostPrice() - (5.0 * 10));

        BusinessEntityResponseDto businessEntityFalse = new BusinessEntityResponseDto(2L, "Compass One", "Sengkang", "Shop", false,false);

        when(mockProductService.getProductById(1L)).thenReturn(product);
        when(mockBusinessEntityClient.getBusinessEntity(101L)).thenReturn(businessEntityFalse);
        when(mockBusinessEntityClient.getBusinessEntity(201L)).thenReturn(businessEntityFalse);
        when(mockInventoryService.getInventoryByProductIdAndBusinessEntityId(1L, 101L))
                .thenReturn(sourceInventoryResponse);
        // Destination inventory does not exist.
        when(mockInventoryService.getInventoryByProductIdAndBusinessEntityId(1L, 201L))
                .thenReturn(null);
        // Stub saveInventory to return the passed inventory
        when(mockInventoryService.saveInventory(any(Inventory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(mockInventoryService.updateInventory(1L, updatedSourceInventory))
                .thenReturn(updatedSourceInventory);
        when(mockInventoryTransactionRepository.save(transaction)).thenReturn(transaction);

        // Act
        InventoryTransactionResponseDto result = inventoryTransactionService.saveInventoryTransaction(transaction);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.productId());
        assertEquals(101L, result.source());
        assertEquals(201L, result.destination());
        assertEquals(10, result.quantity());
        assertEquals(5.0, result.costPricePerUnit(), 0.01);

        verify(mockProductService, times(1)).getProductById(1L);
        verify(mockInventoryService, times(1))
                .getInventoryByProductIdAndBusinessEntityId(1L, 101L);
        verify(mockInventoryService, times(1))
                .updateInventory(1L, updatedSourceInventory);
        verify(mockInventoryService, times(1))
                .getInventoryByProductIdAndBusinessEntityId(1L, 201L);
        verify(mockInventoryService, times(1))
                .saveInventory(argThat(inv ->
                        inv.getProductId() == 1L &&
                        inv.getBusinessEntityId() == 201L &&
                        inv.getQuantity() == 10 &&
                        inv.getTotalCostPrice() == 5.0 * 10));
        verify(mockInventoryTransactionRepository, times(1)).save(transaction);
    }
}
package com.retailpulse.controller;

import com.retailpulse.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.retailpulse.dto.request.InventoryUpdateRequestDto;
import com.retailpulse.service.InventoryService;
import com.retailpulse.service.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class InventoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController)
                .setControllerAdvice(new GlobalExceptionHandler()) 
                .build();
    }

    @Test
    void testSalesUpdateStocks_success() throws Exception {
        InventoryUpdateRequestDto.InventoryItem item = new InventoryUpdateRequestDto.InventoryItem(100L, 5);
        InventoryUpdateRequestDto request = new InventoryUpdateRequestDto(1L, List.of(item));

        mockMvc.perform(post("/api/inventory/salesUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(inventoryService).salesUpdateStocks(request);
    }

    @Test
    void testSalesUpdateStocks_businessException() throws Exception {
        InventoryUpdateRequestDto.InventoryItem item = new InventoryUpdateRequestDto.InventoryItem(100L, 999);
        InventoryUpdateRequestDto request = new InventoryUpdateRequestDto(1L, List.of(item));

        doThrow(new BusinessException("INSUFFICIENT_STOCK", "Not enough stock"))
                .when(inventoryService).salesUpdateStocks(Mockito.any());

        mockMvc.perform(post("/api/inventory/salesUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INSUFFICIENT_STOCK"))
                .andExpect(jsonPath("$.message").value("Not enough stock"));
    }
}

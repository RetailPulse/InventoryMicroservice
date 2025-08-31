package com.retailpulse.repository;

import com.retailpulse.dto.response.InventoryTransactionProductResponseDto;
import com.retailpulse.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, UUID> {

    @Query("SELECT new com.retailpulse.controller.response.InventoryTransactionProductResponseDto(it, p) FROM InventoryTransaction it JOIN Product p ON it.productId = p.id")
    List<InventoryTransactionProductResponseDto> findAllWithProduct();

    // Todo: Implement in controller
//    @Query("SELECT new com.retailpulse.DTO.InventoryTransactionDetailsDto(it, p, source, destination) " +
//            "FROM InventoryTransaction it JOIN Product p ON it.productId = p.id " +
//            "JOIN BusinessEntity source ON it.source = source.id " +
//            "JOIN BusinessEntity destination ON it.destination = destination.id " +
//            "WHERE it.insertedAt >= :startDateTime AND it.insertedAt <= :endDateTime ORDER BY it.insertedAt ASC")
//    List<InventoryTransactionDetailsDto> findAllWithProductAndBusinessEntity(@Param("startDateTime") Instant startDateTime,
//                                                                             @Param("endDateTime") Instant endDateTime);

}

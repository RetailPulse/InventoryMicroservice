package com.retailpulse.repository;

import com.retailpulse.DTO.InventoryTransactionDetailsDto;
import com.retailpulse.DTO.InventoryTransactionProductDto;
import com.retailpulse.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, UUID> {

    @Query("SELECT new com.retailpulse.DTO.InventoryTransactionProductDto(it, p) FROM InventoryTransaction it JOIN Product p ON it.productId = p.id")
    List<InventoryTransactionProductDto> findAllWithProduct();

    // Todo: Implement in controller
//    @Query("SELECT new com.retailpulse.DTO.InventoryTransactionDetailsDto(it, p, source, destination) " +
//            "FROM InventoryTransaction it JOIN Product p ON it.productId = p.id " +
//            "JOIN BusinessEntity source ON it.source = source.id " +
//            "JOIN BusinessEntity destination ON it.destination = destination.id " +
//            "WHERE it.insertedAt >= :startDateTime AND it.insertedAt <= :endDateTime ORDER BY it.insertedAt ASC")
//    List<InventoryTransactionDetailsDto> findAllWithProductAndBusinessEntity(@Param("startDateTime") Instant startDateTime,
//                                                                             @Param("endDateTime") Instant endDateTime);

}

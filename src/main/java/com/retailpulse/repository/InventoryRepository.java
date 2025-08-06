package com.retailpulse.repository;

import com.retailpulse.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductIdAndBusinessEntityId(long productId, long businessEntityId);
    List<Inventory> findByProductId(Long productId);
    List<Inventory> findByBusinessEntityId(Long businessEntityId);
}

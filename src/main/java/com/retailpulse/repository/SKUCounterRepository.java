package com.retailpulse.repository;

import com.retailpulse.entity.SKUCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SKUCounterRepository extends JpaRepository<SKUCounter, Long> {
    Optional<SKUCounter> findByName(String name);

    @Modifying
    @Query(value = "UPDATE skucounter SET counter = LAST_INSERT_ID(counter + 1) WHERE name = :name", nativeQuery = true)
    void incrementAndStore(@Param("name") String name);

    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Long getLastInsertedId();

}

package com.retailpulse.service;

import com.retailpulse.entity.SKUCounter;
import com.retailpulse.repository.SKUCounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SKUGeneratorService {
    private static final String COUNTER_NAME = "product";
    private final SKUCounterRepository skuCounterRepository;

    @Autowired
    public SKUGeneratorService(SKUCounterRepository skuCounterRepository) {
        this.skuCounterRepository = skuCounterRepository;
    }

    @Transactional
    public String generateSKU() {
        // Find or create a SKU counter
        SKUCounter skuCounter = skuCounterRepository.findByName(COUNTER_NAME)
                .orElseGet(() -> {
                    SKUCounter newCounter = new SKUCounter(COUNTER_NAME, 0L);
                    return skuCounterRepository.save(newCounter);
                });

        // Atomically increment
        skuCounterRepository.incrementAndStore(COUNTER_NAME);

        // Get the new incremented value
        Long newCounter = skuCounterRepository.getLastInsertedId();

        // Return the SKU in the format "RP1", "RP2", "RP3", etc.
        return "RP" + newCounter;
    }
}

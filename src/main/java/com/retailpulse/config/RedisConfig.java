package com.retailpulse.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.retailpulse.dto.InventoryTransactionDetailsDto;
import com.retailpulse.dto.InventoryTransactionProductDto;
import com.retailpulse.dto.response.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class RedisConfig {
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // Base config: key serializer + TTL, do not cache nulls
        RedisCacheConfiguration base = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));

        // Plain ObjectMapper (no default typing)
        ObjectMapper om = new ObjectMapper().findAndRegisterModules();

        // === Per-type serializers (no default typing) ===
//        // BusinessEntity
//        Jackson2JsonRedisSerializer<BusinessEntityResponseDto> beSer = new Jackson2JsonRedisSerializer<>(om, BusinessEntityResponseDto.class);
//        JavaType beListType = om.getTypeFactory().constructCollectionType(List.class, BusinessEntityResponseDto.class);
//        Jackson2JsonRedisSerializer<Object> beListSer = new Jackson2JsonRedisSerializer<>(om, beListType);

        // Inventory
        Jackson2JsonRedisSerializer<InventoryResponseDto> invSer = new Jackson2JsonRedisSerializer<>(om, InventoryResponseDto.class);
        JavaType invListType = om.getTypeFactory().constructCollectionType(List.class, InventoryResponseDto.class);
        Jackson2JsonRedisSerializer<Object> invListSer = new Jackson2JsonRedisSerializer<>(om, invListType);

        // InventoryTransactionProduct (Response)
        Jackson2JsonRedisSerializer<InventoryTransactionProductResponseDto> itpRespSer = new Jackson2JsonRedisSerializer<>(om, InventoryTransactionProductResponseDto.class);
        JavaType itpRespListType = om.getTypeFactory().constructCollectionType(List.class, InventoryTransactionProductResponseDto.class);
        Jackson2JsonRedisSerializer<Object> itpRespListSer = new Jackson2JsonRedisSerializer<>(om, itpRespListType);

        // InventoryTransaction (Response)
        Jackson2JsonRedisSerializer<InventoryTransactionResponseDto> itrSer = new Jackson2JsonRedisSerializer<>(om, InventoryTransactionResponseDto.class);
        JavaType itrListType = om.getTypeFactory().constructCollectionType(List.class, InventoryTransactionResponseDto.class);
        Jackson2JsonRedisSerializer<Object> itrListSer = new Jackson2JsonRedisSerializer<>(om, itrListType);

        // Product (Response)
        Jackson2JsonRedisSerializer<ProductResponseDto> prodSer = new Jackson2JsonRedisSerializer<>(om, ProductResponseDto.class);
        JavaType prodListType = om.getTypeFactory().constructCollectionType(List.class, ProductResponseDto.class);
        Jackson2JsonRedisSerializer<Object> prodListSer = new Jackson2JsonRedisSerializer<>(om, prodListType);

        // InventoryTransactionDetails (DTO)
        Jackson2JsonRedisSerializer<InventoryTransactionDetailsDto> itdSer = new Jackson2JsonRedisSerializer<>(om, InventoryTransactionDetailsDto.class);
        JavaType itdListType = om.getTypeFactory().constructCollectionType(List.class, InventoryTransactionDetailsDto.class);
        Jackson2JsonRedisSerializer<Object> itdListSer = new Jackson2JsonRedisSerializer<>(om, itdListType);

        // InventoryTransactionProduct (DTO)
        Jackson2JsonRedisSerializer<InventoryTransactionProductDto> itpDtoSer = new Jackson2JsonRedisSerializer<>(om, InventoryTransactionProductDto.class);
        JavaType itpDtoListType = om.getTypeFactory().constructCollectionType(List.class, InventoryTransactionProductDto.class);
        Jackson2JsonRedisSerializer<Object> itpDtoListSer = new Jackson2JsonRedisSerializer<>(om, itpDtoListType);



        // Per-cache configurations with the correct value serializer
        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
//        cacheConfigs.put("businessEntity", base.serializeValuesWith(
//                RedisSerializationContext.SerializationPair.fromSerializer(beSer))
//        );
//        cacheConfigs.put("businessEntityList", base.serializeValuesWith(
//                RedisSerializationContext.SerializationPair.fromSerializer(beListSer))
//        );

        cacheConfigs.put("inventory", base.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(invSer))
        );
        cacheConfigs.put("inventoryList", base.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(invListSer))
        );

        cacheConfigs.put("inventoryTransactionProduct", base.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(itpRespSer))
        );
        cacheConfigs.put("inventoryTransactionProductList", base.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(itpRespListSer))
        );

        cacheConfigs.put("inventoryTransaction", base.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(itrSer))
        );
        cacheConfigs.put("inventoryTransactionList", base.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(itrListSer))
        );

        cacheConfigs.put("product", base.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(prodSer))
        );
        cacheConfigs.put("productList", base.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(prodListSer))
        );

        cacheConfigs.put("inventoryTransactionDetails", base.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(itdSer))
        );
        cacheConfigs.put("inventoryTransactionDetailsList", base.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(itdListSer))
        );

        cacheConfigs.put("inventoryTransactionProductDto", base.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(itpDtoSer))
        );
        cacheConfigs.put("inventoryTransactionProductDtoList", base.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(itpDtoListSer))
        );

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(base) // default if any other cache is added later
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }
}

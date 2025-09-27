package com.retailpulse.client;

import com.retailpulse.dto.response.BusinessEntityResponseDto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
  name = "business-entity",
  url = "${businessentity-service.url}",
  configuration = com.retailpulse.config.FeignConfig.class
  )
public interface BusinessEntityClient {
    @GetMapping("/api/businessEntity")
    List<BusinessEntityResponseDto> getAllBusinessEntity();

    @GetMapping("/api/businessEntity/{businessEntityId}")
    BusinessEntityResponseDto getBusinessEntity(@PathVariable("businessEntityId") Long businessEntityId);
}
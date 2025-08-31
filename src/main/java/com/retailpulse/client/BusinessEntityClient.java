package com.retailpulse.client;

import com.retailpulse.dto.response.BusinessEntityResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "business-entity", url = "http://localhost:8085")
public interface BusinessEntityClient {
    @GetMapping("/api/businessEntity/{businessEntityId}")
    BusinessEntityResponseDto getBusinessEntity(@PathVariable("businessEntityId") Long businessEntityId);
}
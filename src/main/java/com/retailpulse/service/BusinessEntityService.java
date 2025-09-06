package com.retailpulse.service;

import com.retailpulse.client.BusinessEntityClient;
import com.retailpulse.dto.response.BusinessEntityResponseDto;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BusinessEntityService {
    private static final Logger log = LoggerFactory.getLogger(BusinessEntityService.class);

    private final BusinessEntityClient businessEntityClient;

    public BusinessEntityService(BusinessEntityClient businessEntityClient) {
        this.businessEntityClient = businessEntityClient;
    }

    public boolean isValidBusinessEntity(Long businessEntityId) {
        try {
            BusinessEntityResponseDto response = businessEntityClient.getBusinessEntity(businessEntityId);
            if (response == null) {
                log.warn("Business entity {} cannot be retrieved (null response)", businessEntityId);
                throw new IllegalArgumentException("Business entity cannot be retrieved (null response): " + businessEntityId);
            }
            return Boolean.TRUE.equals(response.active());
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isExternalBusinessEntity(Long businessEntityId) {
        if (businessEntityId == null) {
            log.debug("isExternalBusinessEntity called with null businessEntityId");
            throw new IllegalArgumentException("businessEntityId cannot be null");
        }

        try {
            BusinessEntityResponseDto response = businessEntityClient.getBusinessEntity(businessEntityId);
            if (response == null) {
                log.warn("Business entity {} cannot be retrieved (null response)", businessEntityId);
                throw new IllegalArgumentException("Business entity cannot be retrieved (null response): " + businessEntityId);
            }
            return response.external();
        } catch (Exception e) {
            log.error("Failed to fetch business entity {}: {}", businessEntityId, e.getMessage(), e);
            throw new IllegalStateException("Unable to fetch business entity with id: " + businessEntityId, e);
        }
    }
}
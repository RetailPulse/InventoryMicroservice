package com.retailpulse.client;

import com.retailpulse.client.DTO.BusinessEntityResponseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class BusinessEntityClient {
    private final RestTemplate restTemplate;
    private final String BusinessEntityURL = "http://localhost:8085/api/businessEntity";

    public BusinessEntityClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isValidBusinessEntity(Long businessEntityId) {
        String url = BusinessEntityURL + "/{businessEntityId}";
        try {
            BusinessEntityResponseDto response = restTemplate.getForObject(url, BusinessEntityResponseDto.class, businessEntityId);
            return response != null;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 400) {
                return false;
            }
            throw e; // rethrow other errors
        } catch (Exception e) {
            return false;
        }
    }
}
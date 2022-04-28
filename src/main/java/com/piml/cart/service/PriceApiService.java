package com.piml.cart.service;


import com.piml.cart.dto.PriceDto;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriceApiService {
    private static final String PRODUCT_API_URI = "https://63d5a2e8-0150-492a-bf0d-16828f348d77.mock.pstmn.io";
    private static final String PRODUCTS_RESOURCE = "/api/v1/fresh-products";
    private final RestTemplate restTemplate;


    public PriceApiService (RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public PriceDto[] fetchPricesById(List<Long> ids) {
        String idsString = ids.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(","));
        String resourceURI = PRODUCT_API_URI.concat(PRODUCTS_RESOURCE).concat("/").concat(idsString);
        try{

            ResponseEntity<PriceDto[]> result = restTemplate.getForEntity(resourceURI, PriceDto[].class);
            return result.getBody();
        } catch (RuntimeException ex) {
            throw new RuntimeException("Product not Found!");
        }
    }

}

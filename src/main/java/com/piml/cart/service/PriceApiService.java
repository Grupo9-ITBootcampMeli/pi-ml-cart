package com.piml.cart.service;


import com.piml.cart.dto.PriceDto;
import com.piml.cart.util.Utils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriceApiService {
    private static final String PRODUCT_API_URI = "http://products:8081";
    private static final String PRODUCTS_RESOURCE = "/api/v1/fresh-products/?products=";
    private final RestTemplate restTemplate;


    public PriceApiService (RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public List<PriceDto> fetchPricesById(List<Long> ids) {
        String resourceURI = Utils.makeURIWithIds(PRODUCT_API_URI, PRODUCTS_RESOURCE, ids);
        try{
            ResponseEntity<PriceDto[]> result = restTemplate.getForEntity(resourceURI, PriceDto[].class);
            return Arrays.stream(result.getBody()).collect(Collectors.toList());
        } catch (RuntimeException ex) {
            throw new RuntimeException("Product not Found!");
            // TODO: 03/05/22 implement exception handling 
        }
    }

}

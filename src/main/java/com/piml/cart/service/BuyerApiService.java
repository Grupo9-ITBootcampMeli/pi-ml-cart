package com.piml.cart.service;

import com.piml.cart.dto.BuyerDto;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BuyerApiService {
    private static final String BUYER_API_URI = "http://gandalf:8080";
    private static final String API_RESOURCE = "/user/v1";

    private final RestTemplate restTemplate;

    // TODO: 03/05/22 put errorHandler 
    public BuyerApiService (RestTemplateBuilder builder) {
        this.restTemplate = builder
                .build();
    }

    public BuyerDto create(BuyerDto buyerDto) {
        String resourceURI = BUYER_API_URI.concat(API_RESOURCE);

        ResponseEntity<BuyerDto> result = restTemplate.postForEntity(resourceURI, buyerDto, BuyerDto.class);
        return result.getBody();
    }

    public BuyerDto getById(Long id) {
        String resourceURI = BUYER_API_URI.concat(API_RESOURCE).concat("/").concat(String.valueOf(id));
        try {
            ResponseEntity<BuyerDto> result = restTemplate.getForEntity(resourceURI, BuyerDto.class);
            return result.getBody();
        } catch (RuntimeException ex) {
            throw new RuntimeException(("Something went wrong...."));
        }
    }
}

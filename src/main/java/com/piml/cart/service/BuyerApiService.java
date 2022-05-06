package com.piml.cart.service;

import com.piml.cart.dto.BuyerDto;
import com.piml.cart.exception.BuyerAlreadyExistsException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;

@Service
public class BuyerApiService {
    private static final String BUYER_API_URI = "http://gandalf:8080";
    private static final String API_RESOURCE = "/user/v1";

    private final RestTemplate restTemplate;

    public BuyerApiService (RestTemplateBuilder builder) {
        this.restTemplate = builder
                .build();
    }

    public BuyerDto create(BuyerDto buyerDto) {
        String resourceURI = BUYER_API_URI.concat(API_RESOURCE);
        try {
            ResponseEntity<BuyerDto> result = restTemplate.postForEntity(resourceURI, buyerDto, BuyerDto.class);
            return result.getBody();
        } catch (HttpClientErrorException ex) {
            throw new BuyerAlreadyExistsException("Buyer ".concat(buyerDto.getName()).concat(" already exists."));
        }
    }

    public BuyerDto getById(Long id) {
        String resourceURI = BUYER_API_URI.concat(API_RESOURCE).concat("/").concat(String.valueOf(id));
        try{
            ResponseEntity<BuyerDto> result = restTemplate.getForEntity(resourceURI, BuyerDto.class);
            return result.getBody();
        }catch(EntityNotFoundException ex){
            throw new EntityNotFoundException("Buyer not found");
        }
    }
}

package com.piml.cart.service;

import com.piml.cart.dto.CartProductDto;
import com.piml.cart.dto.WarehouseStockDto;
import com.piml.cart.util.Utils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class WarehouseApiService {
    private static final String WAREHOUSE_API_URL = "http://warehouse:8083";
    private static final String WAREHOUSE_RESOURCE_1 = "/api/v1/fresh-products/list?products=";
    private static final String WAREHOUSE_RESOURCE_2 = "/api/v1/fresh-products/";

    private final RestTemplate restTemplate;

    public WarehouseApiService(RestTemplateBuilder builder) { this.restTemplate = builder.build(); }

    public List<WarehouseStockDto> fetchWarehousesById(List<Long> ids) {
        String resourceURI = Utils.makeURIWithIds(WAREHOUSE_API_URL, WAREHOUSE_RESOURCE_1, ids);
        try{
            ResponseEntity<WarehouseStockDto[]> result = restTemplate
                    .getForEntity(resourceURI, WarehouseStockDto[].class);
            WarehouseStockDto[] listWarehouses = result.getBody();
            return Arrays
                    .stream(listWarehouses).collect(Collectors.toList());
        } catch (HttpClientErrorException ex){
            throw new EntityNotFoundException("Product not found in stock!");
        }
    }

    public HttpStatus stockAdjust (List<CartProductDto> productList) {
        String resourceUri = WAREHOUSE_API_URL.concat(WAREHOUSE_RESOURCE_2);
        try{
        ResponseEntity<Object> result = restTemplate.postForEntity(resourceUri, productList, Object.class);
        return result.getStatusCode();
        } catch (HttpClientErrorException ex){
            throw new EntityNotFoundException("Product not found in stock!");
    }
}
}

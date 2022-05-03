package com.piml.cart.service;

import com.piml.cart.dto.WarehouseStockDto;
import com.piml.cart.util.Utils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class WarehouseApiService {
    private static final String WAREHOUSE_API_URL = "https://63d5a2e8-0150-492a-bf0d-16828f348d77.mock.pstmn.io";
    private static final String WAREHOUSE_RESOURCE = "/api/v1/fresh-products/list?products=";
    private final RestTemplate restTemplate;

    public WarehouseApiService(RestTemplateBuilder builder) { this.restTemplate = builder.build(); }

    public List<WarehouseStockDto> fetchWarehousesById(List<Long> ids) {
        String resourceURI = Utils.makeURIWithIds(WAREHOUSE_API_URL, WAREHOUSE_RESOURCE, ids);
        try{
            ResponseEntity<WarehouseStockDto[]> result = restTemplate
                    .getForEntity(resourceURI, WarehouseStockDto[].class);
            WarehouseStockDto[] listWarehouses = result.getBody();
            return Arrays
                    .stream(listWarehouses).collect(Collectors.toList());
        }catch (RuntimeException ex){
            throw new RuntimeException("Product not found!");
        }
    }
}

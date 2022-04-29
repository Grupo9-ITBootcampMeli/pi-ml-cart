package com.piml.cart.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WarehouseApiService {
    private static final String WAREHOUSE_API_URL = "";
    private static final String WAREHOUSE_RESOURCE = "";
    private final RestTemplate restTemplate;

    public WarehouseApiService(RestTemplateBuilder builder) { this.restTemplate = builder.build(); }

//    public List<> fetchStockById(List<String> ids) {
//        String idsString = ids.stream().collect(Collectors.joining(","));
//    }
}

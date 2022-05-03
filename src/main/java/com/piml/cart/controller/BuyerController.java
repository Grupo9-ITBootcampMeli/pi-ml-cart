package com.piml.cart.controller;

import com.piml.cart.dto.BuyerDto;
import com.piml.cart.dto.BuyerResponseDto;
import com.piml.cart.service.BuyerApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping
public class BuyerController {

    private final BuyerApiService buyerApiService;
    public BuyerController(BuyerApiService buyerApiService) {
        this.buyerApiService = buyerApiService;
    }

    @PostMapping("/buyer/v1")
    public ResponseEntity<BuyerResponseDto> createBuyer(@RequestBody BuyerDto buyer) {
        BuyerDto createdBuyer = buyerApiService.create(buyer);
        BuyerResponseDto returnBuyer = BuyerResponseDto.map(createdBuyer);
        return new ResponseEntity<>(returnBuyer, HttpStatus.CREATED);
    }

    @GetMapping("/buyer/v1/{id}")
    public ResponseEntity<BuyerDto> getBuyerById(@PathVariable Long id) {
        BuyerDto foundBuyer = buyerApiService.getById(id);
        return ResponseEntity.ok(foundBuyer);
    }
}

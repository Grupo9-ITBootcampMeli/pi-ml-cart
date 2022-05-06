package com.piml.cart.controller;

import com.piml.cart.dto.BuyerDto;
import com.piml.cart.dto.BuyerResponseDto;
import com.piml.cart.service.BuyerApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Api(value = "Buyer")
@RestController
@RequestMapping
public class BuyerController {

    private final BuyerApiService buyerApiService;
    public BuyerController(BuyerApiService buyerApiService) {
        this.buyerApiService = buyerApiService;
    }


    /**
     * POST method to create a buyer user
     * @param buyer is a dto that represents the requestBody that will be sent to the user API in order
     *              to create a new user with the buyer role.
     * @RequestBody  carries the payload used to create the entity and persist it in the user API repository.
     * @return the created buyer information as a dto.
     */


    @ApiOperation(value = "Register a new Buyer")
    @PostMapping("/buyer/v1")
    public ResponseEntity<BuyerResponseDto> createBuyer(@RequestBody BuyerDto buyer) {
        BuyerDto createdBuyer = buyerApiService.create(buyer);
        BuyerResponseDto returnBuyer = BuyerResponseDto.map(createdBuyer);
        return new ResponseEntity<>(returnBuyer, HttpStatus.CREATED);
    }


    /**
     * GET method to retrieve the buyer user information
     * @param the user id of the buyer user
     * @return the buyer user information retrieved from the user api
     */

    @ApiOperation(value = "Find Buyer by ID")
    @GetMapping("/buyer/v1/{id}")
    public ResponseEntity<BuyerDto> getBuyerById(@PathVariable Long id) {
        BuyerDto foundBuyer = buyerApiService.getById(id);
        return ResponseEntity.ok(foundBuyer);
    }
}

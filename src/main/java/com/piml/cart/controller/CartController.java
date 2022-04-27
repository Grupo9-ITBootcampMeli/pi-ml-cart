package com.piml.cart.controller;


import com.piml.cart.dto.CartDto;
import com.piml.cart.entity.Cart;
import com.piml.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/api/v1/fresh-products/orders/")
    public ResponseEntity<CartDto> createCart(@RequestBody CartDto dto) {
        Cart cart = dto.map();
        CartDto createdCart = CartDto.map(cartService.create(cart));
        return new ResponseEntity<>(createdCart, HttpStatus.CREATED);
    }
}

package com.piml.cart.controller;


import com.piml.cart.dto.CartDto;
import com.piml.cart.dto.CartProductDto;
import com.piml.cart.dto.ResponseDto;
import com.piml.cart.entity.Cart;
import com.piml.cart.entity.CartProduct;
import com.piml.cart.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class CartController {

    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/api/v1/fresh-products/orders/")
    public ResponseEntity<ResponseDto> createCart(@RequestBody CartDto dto) {
        Cart cart = CartDto.map(dto);
        ResponseDto response = new ResponseDto(cartService.create(cart));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/fresh-products/orders/")
    public ResponseEntity<List<CartProductDto>> getCartProducts(@RequestParam(name = "querytype") Long id) {
        List<CartProduct> cartProductList = cartService.getCartProducts(id);
        return new ResponseEntity<>(cartProductList
                .stream()
                .map(CartProductDto::map).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping("/api/v1/fresh-products/orders/")
    public ResponseEntity<HttpStatus> updateCartStatus(@RequestParam(name = "id") Long id) {
        Cart cartToUpdate = cartService.getCartById(id);
        cartService.updateCartStatus(cartToUpdate);
        return new ResponseEntity(HttpStatus.OK);
    }
}

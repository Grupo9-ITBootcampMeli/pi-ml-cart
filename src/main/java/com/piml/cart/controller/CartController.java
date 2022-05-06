package com.piml.cart.controller;


import com.piml.cart.dto.CartDto;
import com.piml.cart.dto.CartProductDto;
import com.piml.cart.dto.ResponseDto;
import com.piml.cart.entity.Cart;
import com.piml.cart.entity.CartProduct;
import com.piml.cart.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(value = "Cart")
@RestController
@RequestMapping
public class CartController {

    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @ApiOperation(value = "Register a new Cart")
    @PostMapping("/api/v1/fresh-products/orders/")
    public ResponseEntity<ResponseDto> createCart(@RequestBody CartDto dto) {
        Cart cart = CartDto.map(dto);
        ResponseDto response = new ResponseDto(cartService.create(cart));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "List products on Cart")
    @GetMapping("/api/v1/fresh-products/orders/")
    public ResponseEntity<List<CartProductDto>> getCartProducts(@RequestParam(name = "products") Long id) {
        List<CartProduct> cartProductList = cartService.getCartProducts(id);
        return new ResponseEntity<>(cartProductList
                .stream()
                .map(CartProductDto::map).collect(Collectors.toList()), HttpStatus.OK);
    }

    @ApiOperation(value = "Update Cart status")
    @PutMapping("/api/v1/fresh-products/orders/")
    public ResponseEntity<String> updateCartStatus(@RequestParam(name = "id") Long id) {
        Cart cartToUpdate = cartService.getCartById(id);
        Cart updatedCart = cartService.updateCartStatus(cartToUpdate);
        String responseString = "Order status successfully updated to: ";
        return new ResponseEntity(responseString.concat(updatedCart.getOrderStatus()), HttpStatus.OK);
    }
}

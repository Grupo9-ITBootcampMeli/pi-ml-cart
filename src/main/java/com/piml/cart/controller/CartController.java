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


    /**
     * POST method to create an cart.
     * @param dto represents the requestBody that generates cart payload.
     * @RequestBody  carries the payload used to create the entity and persist it in the repository.
     * @return the total price of the order created in case of a success integration with other API's.
     */


    @ApiOperation(value = "Register a new Cart")
    @PostMapping("/api/v1/fresh-products/orders/")
    public ResponseEntity<ResponseDto> createCart(@RequestBody CartDto dto) {
        Cart cart = CartDto.map(dto);
        ResponseDto response = new ResponseDto(cartService.create(cart));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    /**
     * GET method to found a section according to Id
     * @param cartId of the cart the client with to get the products from
     * @return a list of the products present in the cart
     */


    @ApiOperation(value = "List products on Cart")
    @GetMapping("/api/v1/fresh-products/orders/")
    public ResponseEntity<List<CartProductDto>> getCartProducts(@RequestParam(name = "products") Long cartId) {
        List<CartProduct> cartProductList = cartService.getCartProducts(cartId);
        return new ResponseEntity<>(cartProductList
                .stream()
                .map(CartProductDto::map).collect(Collectors.toList()), HttpStatus.OK);
    }


    /**
     * PUT method to update order status to "closed"
     * @param  id of the cart generate order payload
     * @return the string signaling the success of the request or an error message saying the cart is already closed
     */


    @ApiOperation(value = "Update Cart status")
    @PutMapping("/api/v1/fresh-products/orders/")
    public ResponseEntity<String> updateCartStatus(@RequestParam(name = "id") Long id) {
        Cart cartToUpdate = cartService.getCartById(id);
        Cart updatedCart = cartService.updateCartStatus(cartToUpdate);
        String responseString = "Order status successfully updated to: ";
        return new ResponseEntity(responseString.concat(updatedCart.getOrderStatus()), HttpStatus.OK);
    }
}

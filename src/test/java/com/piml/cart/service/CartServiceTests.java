package com.piml.cart.service;

import com.piml.cart.entity.Cart;
import com.piml.cart.entity.CartProduct;
import com.piml.cart.repository.CartProductRepository;
import com.piml.cart.repository.CartRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class CartServiceTests {
    private CartService cartService;
    private CartRepository cartRepositoryMock;
    private CartProductRepository cartProductRepositoryMock;
    private PriceApiService priceApiServiceMock;
    private WarehouseApiService warehouseApiServiceMock;

    @BeforeEach
    public void before() {
        cartRepositoryMock = Mockito.mock(CartRepository.class);
        cartProductRepositoryMock  = Mockito.mock(CartProductRepository.class);
        priceApiServiceMock = Mockito.mock(PriceApiService.class);
        warehouseApiServiceMock = Mockito.mock(WarehouseApiService.class);

        cartService = new CartService(cartRepositoryMock, cartProductRepositoryMock, priceApiServiceMock, warehouseApiServiceMock);
    }

    @Test

    //cenarios: sucesso
    public void shouldCreateCartWhenValidateProductsAndStock() {
        Cart cart = createValidCart();
        cart.setOrderStatus("Fechado");
        Mockito.when(cartRepositoryMock.save(cart)).thenReturn(cart);
        Assertions.assertDoesNotThrow(() -> {

        });
    }

    private Cart createValidCart() {
        Cart cart = Cart.builder()
                .id(1L)
                .buyerId(1L)
                .orderStatus("Aberto")
                .orderDate(LocalDateTime.parse("2021-05-03T05:30:49"))
                .products(new ArrayList<>(Arrays.asList(
                        CartProduct.builder().productId(1L).quantity(2).unitPrice(BigDecimal.valueOf(2.5)).build(),
                        CartProduct.builder().productId(2L).quantity(3).unitPrice(BigDecimal.valueOf(3.5)).build()
                ))).build();
        cart.getProducts().forEach(cartProduct -> cartProduct.setCart(cart));
        return cart;
    }

}

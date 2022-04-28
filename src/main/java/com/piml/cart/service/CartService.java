package com.piml.cart.service;

import com.piml.cart.entity.Cart;
import com.piml.cart.entity.CartProduct;
import com.piml.cart.repository.CartProductRepository;
import com.piml.cart.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final PriceApiService priceApiService;

    public CartService(CartRepository cartRepository, CartProductRepository cartProductRepository, PriceApiService priceApiService) {
        super();
        this.cartRepository = cartRepository;
        this.cartProductRepository = cartProductRepository;
        this.priceApiService = priceApiService;
    }

    public Cart create(Cart cart) {
        Cart registeredCart = cartRepository.save(cart);
        List<CartProduct> cartProducts = setCart(registeredCart);
        cartProducts.stream().map(cartProductRepository::save).collect(Collectors.toList());
//        setCart nos CartProducts
//        save
//      chamada para getPrices
//        calcular preço total
        return cart;
    }
    public List<CartProduct> setCart(Cart cart) {
        List<CartProduct> cartProducts = new ArrayList<>();
        for (CartProduct cp: cart.getProducts()) {
            cp.setCart(cart);
            cartProducts.add(cp);
        }
        return  cartProducts;
    }

}




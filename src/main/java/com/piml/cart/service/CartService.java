package com.piml.cart.service;

import com.piml.cart.entity.Cart;
import com.piml.cart.repository.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        super();
        this.cartRepository = cartRepository;
    }

    public Cart create(Cart cart) { return cartRepository.save(cart); }
}


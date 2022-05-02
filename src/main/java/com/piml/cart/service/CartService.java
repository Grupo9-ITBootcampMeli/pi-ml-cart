package com.piml.cart.service;


import com.piml.cart.dto.PriceDto;
import com.piml.cart.entity.Cart;
import com.piml.cart.entity.CartProduct;
import com.piml.cart.repository.CartProductRepository;
import com.piml.cart.repository.CartRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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
        setPrices(cartProducts);
        cartProducts.stream().map(cartProductRepository::save).collect(Collectors.toList());
        return cart;
    }

    public List<CartProduct> getCartProducts(Long id) {
        return cartRepository.getById(id).getProducts();
    }

    public Cart updateCartStatus(Cart cart) {
        if (cart.getOrderStatus().equals("Aberto")) {
            cart.setOrderStatus("Fechado");
        } else {
            cart.setOrderStatus("Aberto");
        }
        return cartRepository.save(cart);
    }

    public List<CartProduct> setCart(Cart cart) {
        List<CartProduct> cartProducts = new ArrayList<>();
        for (CartProduct cp: cart.getProducts()) {
            cp.setCart(cart);
            cartProducts.add(cp);
        }
        return  cartProducts;
    }

    public Cart getCartById(Long id) {
        return cartRepository.findById(id).orElseThrow(() -> {
           throw new EntityNotFoundException("Cart not found");
        });
    }

    public void setPrices(List<CartProduct> cartProducts) {
        List<Long> ids = cartProducts.stream().map(p -> p.getProductId()).collect(Collectors.toList());
        List<PriceDto> prices = this.priceApiService.fetchPricesById(ids);
        cartProducts.forEach(cartProduct -> cartProduct.setUnitPrice(prices.get(cartProducts.indexOf(cartProduct)).getPrice()));
    }

}




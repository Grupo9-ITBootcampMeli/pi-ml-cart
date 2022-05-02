package com.piml.cart.service;


import com.piml.cart.dto.PriceDto;
import com.piml.cart.dto.WarehouseStockDto;
import com.piml.cart.entity.Cart;
import com.piml.cart.entity.CartProduct;
import com.piml.cart.repository.CartProductRepository;
import com.piml.cart.repository.CartRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final PriceApiService priceApiService;
    private final WarehouseApiService warehouseApiService;

    public CartService(CartRepository cartRepository, CartProductRepository cartProductRepository, PriceApiService priceApiService, WarehouseApiService warehouseApiService) {
        super();
        this.cartRepository = cartRepository;
        this.cartProductRepository = cartProductRepository;
        this.priceApiService = priceApiService;
        this.warehouseApiService = warehouseApiService;
    }

    public Cart create(Cart cart) {
        Cart registeredCart = cartRepository.save(cart);
        List<CartProduct> cartProducts = setCart(registeredCart);
        setPrices(cartProducts);
        cartProducts.stream().map(cartProductRepository::save).collect(Collectors.toList());
        Map<Long, Integer> warehouseStock = getProductQttyStock(cartProducts);
        System.out.println(warehouseStock);
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

    private List<CartProduct> validateCartProducts(Cart cart) {
        List<CartProduct> products = cart.getProducts();


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
        List<Long> ids = CartService.getProductIds(cartProducts);
        List<PriceDto> prices = this.priceApiService.fetchPricesById(ids);
        cartProducts.forEach(cartProduct -> cartProduct.setUnitPrice(prices.get(cartProducts.indexOf(cartProduct)).getPrice()));
    }

    public Map<Long, Integer> getProductQttyStock (List<CartProduct> cartProducts) {
        List<Long> ids = CartService.getProductIds(cartProducts);
        List<WarehouseStockDto> warehouses = this.warehouseApiService.fetchWarehousesById(ids);
        return warehouses.stream()
                .map(w -> w.mapQttyByProductId())
                .collect(Collectors.toMap(k -> k.getKey(), k -> k.getValue(), Integer::sum));
    }

    private static List<Long> getProductIds (List<CartProduct> cartProducts) {
        return cartProducts.stream().map(CartProduct::getProductId).collect(Collectors.toList());
    }

}




package com.piml.cart.service;


import com.piml.cart.dto.PriceDto;
import com.piml.cart.dto.WarehouseStockDto;
import com.piml.cart.entity.Cart;
import com.piml.cart.entity.CartProduct;
import com.piml.cart.repository.CartProductRepository;
import com.piml.cart.repository.CartRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final PriceApiService priceApiService;
    private final WarehouseApiService warehouseApiService;

    public CartService(CartRepository cartRepository, CartProductRepository cartProductRepository,
                       PriceApiService priceApiService, WarehouseApiService warehouseApiService) {
        super();
        this.cartRepository = cartRepository;
        this.cartProductRepository = cartProductRepository;
        this.priceApiService = priceApiService;
        this.warehouseApiService = warehouseApiService;
    }

    public Cart create(Cart cart) {
        validateCartProducts(cart);
        Cart registeredCart = cartRepository.save(validateCartProducts(cart));
        List<CartProduct> cartProducts = setCart(registeredCart);
        cartProducts.forEach(cartProductRepository::save);
        return cart;
    }

    public List<CartProduct> getCartProducts(Long id) {
        return cartRepository.getById(id).getProducts();
    }

    public Cart updateCartStatus(Cart cart) {
        if (cart.getOrderStatus().equals("Aberto")) {
            cart.setOrderStatus("Fechado");
        } else {
            throw new RuntimeException("Order has already been closed");
        }
        return cartRepository.save(cart);
    }

    private Cart validateCartProducts(Cart cart) {
        List<CartProduct> registeredProducts = validateProducts(cart.getProducts());
        cart.setProducts(registeredProducts);
        Map<Long, Integer> qttyInWarehouse = getProductQttyStock(registeredProducts);
        return validateQttyInStock(qttyInWarehouse, cart);
    }

    private Cart validateQttyInStock (Map<Long, Integer> qttyInStock, Cart cart) {
        List<CartProduct> cartProducts = cart.getProducts();
        Map<Long, Integer> cartMap = cartProducts.stream()
                .map(CartProduct::mapQttyByProductId)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum));
        mapComparer(qttyInStock, cartMap);
        warehouseApiService.stockAdjust(cartProducts.stream().map(CartProduct::map).collect(Collectors.toList()));
        return cart;
    }

    private void mapComparer (Map<Long, Integer> stock, Map<Long, Integer> cart) {
       stock.forEach((key, value) -> {
           if(cart.get(key) > value) {
               throw new RuntimeException("Product out of stock");
           }
       });
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

    public List<CartProduct> validateProducts(List<CartProduct> cartProducts) {
        List<Long> ids = CartService.getProductIds(cartProducts);
        List<PriceDto> prices = this.priceApiService.fetchPricesById(ids);
        cartProducts.forEach(cartProduct -> cartProduct.setUnitPrice(prices.get(cartProducts.indexOf(cartProduct)).getPrice()));
        return cartProducts;
    }

    private Map<Long, Integer> getProductQttyStock (List<CartProduct> cartProducts) {
        List<Long> ids = CartService.getProductIds(cartProducts);
        List<WarehouseStockDto> warehouses = this.warehouseApiService.fetchWarehousesById(ids);
        return warehouses.stream()
                .map(WarehouseStockDto::mapQttyByProductId)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum));
    }



    private static List<Long> getProductIds (List<CartProduct> cartProducts) {
        return cartProducts.stream().map(CartProduct::getProductId).collect(Collectors.toList());
    }
}




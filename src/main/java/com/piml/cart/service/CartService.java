package com.piml.cart.service;


import com.piml.cart.dto.PriceDto;
import com.piml.cart.dto.WarehouseStockDto;
import com.piml.cart.entity.Cart;
import com.piml.cart.entity.CartProduct;
import com.piml.cart.exception.ClosedCartException;
import com.piml.cart.exception.OutOfStockException;
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

    /**
     * create cart method used to validate the payload received from the controller and persist it in the repository
     * @param cart is validated through methods that interact with the product and warehouse api's
     * @return a cart whose products have been validated at both the product and warehouse API's
     * and has been persisted in the repository
     */

    public Cart create(Cart cart) {
        Cart validCart = validateCartProducts(cart);
        validCart.setOrderStatus("Fechado");
        Cart registeredCart = cartRepository.save(validCart);
        List<CartProduct> cartProducts = setCart(registeredCart);
        cartProducts.forEach(cartProductRepository::save);
        return registeredCart;
    }

    /**
     * method called from the controller with the goal to get the products from a cart based on its id
     * @param id from the cart whose products the client wants to get
     * @return a list of cart products that have been successfully persisted in a cart
     */

    public List<CartProduct> getCartProducts(Long id) {
        return cartRepository.getById(id).getProducts();
    }

    /**
     * method called from the controller with the goal of updating the order status based on the cart id
     * @param cart whose status the client wishes to update
     * @return the cart whose status has been updated or a custom exception
     */

    public Cart updateCartStatus(Cart cart) throws RuntimeException {
        if (cart.getOrderStatus().equals("Aberto")) {
            cart.setOrderStatus("Fechado");
        } else {
            throw new ClosedCartException("Order has already been closed");
        }
        return cartRepository.save(cart);
    }

    /**
     * auxiliary method called from the controller with the goal of
     * retrieving the cart from the repository in order to update its status
     * @param id from the cart to be retrieved from the repository and whose status the client wishes to update
     * @return the cart whose status is to be updated or an exception in case it is not found in the repository
     */

    public Cart getCartById(Long id) {
        return cartRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Cart not found");
        });
    }

    /**
     * auxiliary method called in the create cart function in order to validate that the quantity of the products
     *in the cart with the quantity in stock at the warehouse api
     * @param cart whose products the client wishes to validate and order
     * @return the cart whose products have been adjusted at the warehouse api repositoru or an exception called by
     * another auxiliary method saying the desired products are out of stock
     */


    private Cart validateCartProducts(Cart cart) {
        List<CartProduct> registeredProducts = validateProductsPrices(cart.getProducts());
        cart.setProducts(registeredProducts);
        Map<Long, Integer> qttyInWarehouse = getProductQttyStock(registeredProducts);
        return validateQttyInStock(qttyInWarehouse, cart);
    }

    /**
     * auxiliary method called to campare the stocks in the cart and warehouse
     * @param qttyInStock is a hashmap composed by a key that stores the productId and stores the value corresponding to
     *                    the quantity of this product found in the stock of the warehouse api
     * @param cart is the cart whose productIds and quantities will be compared against the qtty in stock value.
     *             The method converts the productId and quantities of the cartproducts to a hashmap and calls an auxiliary
     *             method to compare to the qttyInStock param
     * @return the cart whose product's quantities have been successfully adjusted from the warehouse API repository
     *  or an exception saying they are out of stock
     */

    private Cart validateQttyInStock (Map<Long, Integer> qttyInStock, Cart cart) {
        List<CartProduct> cartProducts = cart.getProducts();
        Map<Long, Integer> cartMap = cartProducts.stream()
                .map(CartProduct::mapQttyByProductId)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum));
        mapComparer(qttyInStock, cartMap);
        warehouseApiService.stockAdjust(cartProducts.stream().map(CartProduct::map).collect(Collectors.toList()));
        return cart;
    }

    /**
     * auxiliary method called to campare two key value hashmaps
     * @param stock is a hashmap composed by a key that stores the productId and stores the value corresponding to
     *                    the quantity of this product found in the stock of the warehouse api
     * @param cart is the hashmap similar to the stock hashmap, but with the productId's and quantities found in the cart
     * @return the method throws a custom exception in case the stock of an item is lesser than in the cart
     */

    private void mapComparer (Map<Long, Integer> stock, Map<Long, Integer> cart) throws RuntimeException{
       stock.forEach((key, value) -> {
           if(cart.get(key) > value) {
               throw new OutOfStockException(("The Product with ProductId ").concat(String.valueOf(key))
                       .concat(" is out of stock."));
           }
       });
    }

    /**
     * auxiliary method used to set the cart to the cartproducts in order to correctly persist their relationship in the repositories
     * @param cart entity whose products will be associated to
     * @return a list of cartProducts to be persisted in the cartProduct repository correctly associated with the cart
     */

    private List<CartProduct> setCart(Cart cart) {
        List<CartProduct> cartProducts = new ArrayList<>();
        for (CartProduct cp: cart.getProducts()) {
            cp.setCart(cart);
            cartProducts.add(cp);
        }
        return  cartProducts;
    }

    /**
     * auxiliary method used to set the cart to the cartproducts in order to correctly persist their relationship in the repositories
     * @param cartProducts is a list of products whose prices and register the client wants to check by calling
     *                     the products API
     * @return a list of cartProducts with the price information retrieved and confirmed from the products API
     */

    private List<CartProduct> validateProductsPrices(List<CartProduct> cartProducts) {
        List<Long> ids = CartService.getProductIds(cartProducts);
        List<PriceDto> prices = this.priceApiService.fetchPricesById(ids);
        cartProducts.forEach(cartProduct -> cartProduct.setUnitPrice(prices.get(cartProducts.indexOf(cartProduct)).getPrice()));
        return cartProducts;
    }

    /**
     * auxiliary method used to call the warehouse API to retrieve the information of stock based on the productIds
     *  retrieved from the product list in the cart
     * @param cartProducts is a list of products whose ids will be used to check for the stock found in
     *                     the warehouse API
     * @return a hashmap with entries of key value pairs where the key is the id of the product and the value
     * is the quantity found in the stock
     */

    private Map<Long, Integer> getProductQttyStock (List<CartProduct> cartProducts) {
        List<Long> ids = CartService.getProductIds(cartProducts);
        List<WarehouseStockDto> warehouses = this.warehouseApiService.fetchWarehousesById(ids);
        return warehouses.stream()
                .map(WarehouseStockDto::mapQttyByProductId)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum));
    }

    /**
     * auxiliary method used to retrieve the productids from a list of cartProducts
     * @param cartProducts is a list of products whose id's wil lbe extracted and returned as a list
     * @return a list of productIds
     */


    private static List<Long> getProductIds (List<CartProduct> cartProducts) {
        return cartProducts.stream().map(CartProduct::getProductId).collect(Collectors.toList());
    }
    public List<Cart> getAllCarts(){
        return cartRepository.findAll();
    }
}




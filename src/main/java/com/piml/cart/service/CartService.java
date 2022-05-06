package com.piml.cart.service;


import com.piml.cart.dto.*;
import com.piml.cart.entity.Cart;
import com.piml.cart.entity.CartProduct;
import com.piml.cart.exception.ClosedCartException;
import com.piml.cart.exception.EmptyCartException;
import com.piml.cart.exception.OutOfStockException;
import com.piml.cart.repository.CartProductRepository;
import com.piml.cart.repository.CartRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
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
        Cart validCart = validateCartProducts(cart);
        validCart.setOrderStatus("Fechado");
        Cart registeredCart = cartRepository.save(validCart);
        List<CartProduct> cartProducts = setCart(registeredCart);
        cartProducts.forEach(cartProductRepository::save);
        return registeredCart;
    }

    public List<CartProduct> getCartProducts(Long id) {
        return cartRepository.getById(id).getProducts();
    }

    public Cart updateCartStatus(Cart cart) throws RuntimeException {
        if (cart.getOrderStatus().equals("Aberto")) {
            cart.setOrderStatus("Fechado");
        } else {
            throw new ClosedCartException("Order has already been closed");
        }
        return cartRepository.save(cart);
    }

    private Cart validateCartProducts(Cart cart) {
        List<CartProduct> registeredProducts = validateProducts(cart.getProducts());
        cart.setProducts(registeredProducts);
        Map<Long, Integer> qttyInWarehouse = getProductQttyStock(registeredProducts);
        return validateQttyInStock(qttyInWarehouse, cart);
    }

    private Cart validateQttyInStock(Map<Long, Integer> qttyInStock, Cart cart) {
        List<CartProduct> cartProducts = cart.getProducts();
        Map<Long, Integer> cartMap = cartProducts.stream()
                .map(CartProduct::mapQttyByProductId)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum));
        mapComparer(qttyInStock, cartMap);
        warehouseApiService.stockAdjust(cartProducts.stream().map(CartProduct::map).collect(Collectors.toList()));
        return cart;
    }

    private void mapComparer(Map<Long, Integer> stock, Map<Long, Integer> cart) throws RuntimeException {
        stock.forEach((key, value) -> {
            if (cart.get(key) > value) {
                throw new OutOfStockException(("The Product with ProductId ").concat(String.valueOf(key))
                        .concat(" is out of stock."));
            }
        });
    }

    public List<CartProduct> setCart(Cart cart) {
        List<CartProduct> cartProducts = new ArrayList<>();
        for (CartProduct cp : cart.getProducts()) {
            cp.setCart(cart);
            cartProducts.add(cp);
        }
        return cartProducts;
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

    private Map<Long, Integer> getProductQttyStock(List<CartProduct> cartProducts) {
        List<Long> ids = CartService.getProductIds(cartProducts);
        List<WarehouseStockDto> warehouses = this.warehouseApiService.fetchWarehousesById(ids);
        return warehouses.stream()
                .map(WarehouseStockDto::mapQttyByProductId)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum));
    }


    private static List<Long> getProductIds(List<CartProduct> cartProducts) {
        return cartProducts.stream().map(CartProduct::getProductId).collect(Collectors.toList());
    }

    public List<TopSellProductsDTO> calculateTotalCart() {
        List<Cart> listCarts = getAllCarts();
        Map<Long, QtyPriceDTO> totalProducts = new HashMap<Long, QtyPriceDTO>();
        if (listCarts.size() != 0) {
            for (Cart cart : listCarts) {
                for (CartProduct product : cart.getProducts()) {
                    Integer accumulator = 0;
                    if (totalProducts.get(product.getProductId()) == null) {
                        accumulator = product.getQuantity();
                    } else {
                        accumulator = totalProducts.get(product.getProductId()).getQuantity() + product.getQuantity();
                    }
                    totalProducts.put(product.getProductId(), new QtyPriceDTO(product.getUnitPrice(), accumulator, product.getUnitPrice().multiply(BigDecimal.valueOf(accumulator))));
                }
            }
        }
        Set<Long> keySet = totalProducts.keySet();
        ArrayList<Long> listOfKeys = new ArrayList<Long>(keySet);
        List<NameAndIdDTO> nameAndIdList = priceApiService.fetchProductsByNameAndId(listOfKeys);
        return topSellProducts(nameAndIdList,totalProducts);
    }

    public List<Cart> getAllCarts() throws  EmptyCartException {
        List<Cart> cartsList = cartRepository.findAll();
        if(cartsList.size() == 0){
            throw new EmptyCartException("There no sells");
        }
        return cartsList;
    }

    public List<TopSellProductsDTO> topSellProducts(List<NameAndIdDTO> listOfNames, Map<Long, QtyPriceDTO> soldProducts) {
        List<TopSellProductsDTO> finalProducts = new ArrayList<TopSellProductsDTO>();
        for (NameAndIdDTO x : listOfNames) {
            finalProducts.add(new TopSellProductsDTO(x.getId(),x.getName(), soldProducts.get(x.getId()).getQuantity(), soldProducts.get(x.getId()).getPrice(), soldProducts.get(x.getId()).getTotal()));
        }
        return finalProducts;
    }
}




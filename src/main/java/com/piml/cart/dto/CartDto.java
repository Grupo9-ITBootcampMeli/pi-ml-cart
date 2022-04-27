package com.piml.cart.dto;

import com.piml.cart.entity.Cart;
import com.piml.cart.entity.CartProduct;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CartDto {

    private CartInfoDto purchaseOrder;
    private List<CartProductDto> products;

    public static void map(Cart cart) {
        return CartDto.builder()

    }

    public Cart map() {
        Cart cart = Cart.builder().buyerId(this.purchaseOrder.getBuyerId())
                .orderStatus(this.purchaseOrder.getOrderStatus())
                .orderDate(this.purchaseOrder.getDate()).build();
//        this.products.stream().map(e -> )
//        productList.stream().map(ProductDTO::map).collect(Collectors.toList());

        cart.setProducts(this.products.stream().map(CartProductDto::map).map(e -> e.setCart_id(cart.getId())).collect(Collectors.toList()));
        return cart;
    }


//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    private LocalDate dueDate;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
//    private LocalDateTime manufacturingDateTime;
}

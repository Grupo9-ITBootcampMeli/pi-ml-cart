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

//    public static void mapResponse(Cart cart) {
//        return CartDto.builder()
//
//    }

    public Cart map() {
        Cart cart = Cart.builder().buyerId(this.purchaseOrder.getBuyerId())
                .orderStatus(this.purchaseOrder.getOrderStatus())
                .orderDate(this.purchaseOrder.getDate()).build();

        cart.setProducts(this.products.stream().map(CartProductDto::map).collect(Collectors.toList()));
        return cart;
    }

//    pegar aqui direto no preço



//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    private LocalDate dueDate;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
//    private LocalDateTime manufacturingDateTime;
}

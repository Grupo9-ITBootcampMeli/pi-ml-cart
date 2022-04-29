package com.piml.cart.dto;

import com.piml.cart.entity.Cart;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CartDto {

    private LocalDateTime date;
    private Long buyerId;
    private String orderStatus;
    private List<CartProductDto> products;


    public static Cart map(CartDto dto) {
        return Cart.builder().buyerId(dto.getBuyerId())
                .orderStatus(dto.getOrderStatus())
                .orderDate(dto.getDate())
                .products(dto.getProducts().stream().map(CartProductDto::map).collect(Collectors.toList()))
                .build();
    }
}

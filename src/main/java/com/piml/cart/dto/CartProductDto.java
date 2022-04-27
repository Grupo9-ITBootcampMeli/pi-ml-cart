package com.piml.cart.dto;

import com.piml.cart.entity.CartProduct;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartProductDto {
    private String productId;
    private Integer quantity;

    public static CartProduct map(CartProductDto dto) {
        return CartProduct.builder()
                .product_id(dto.getProductId())
                .quantity(dto.getQuantity()).build();
    }
}

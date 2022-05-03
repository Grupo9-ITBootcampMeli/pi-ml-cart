package com.piml.cart.dto;

import com.piml.cart.entity.CartProduct;
import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartProductDto {
    private Long productId;
    private Integer quantity;
    private BigDecimal unitPrice;

    public static CartProduct map(CartProductDto dto) {
        return CartProduct.builder()
                .productId(dto.getProductId())
                .quantity(dto.getQuantity()).build();
    }

    public CartProductDto (Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static CartProductDto map(CartProduct cp) {
        return CartProductDto.builder()
                .productId(cp.getProductId())
                .quantity(cp.getQuantity())
                .unitPrice(cp.getUnitPrice()).build();
    }

}

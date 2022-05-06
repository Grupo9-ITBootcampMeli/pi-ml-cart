package com.piml.cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.piml.cart.entity.CartProduct;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartProductDto {
    @NotNull(message = "Please fill in a valid productId")
    private Long productId;
    @NotNull(message = "Please fill in a valid quantity")
    private Integer quantity;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal unitPrice;

    public static CartProduct map(CartProductDto dto) {
        return CartProduct.builder()
                .productId(dto.getProductId())
                .quantity(dto.getQuantity()).build();
    }


    public static CartProductDto map(CartProduct cp) {
        return CartProductDto.builder()
                .productId(cp.getProductId())
                .quantity(cp.getQuantity())
                .unitPrice(cp.getUnitPrice()).build();
    }

}

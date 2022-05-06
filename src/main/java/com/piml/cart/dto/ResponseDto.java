package com.piml.cart.dto;

import com.piml.cart.entity.Cart;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private BigDecimal totalPrice;
    public ResponseDto(Cart cart) {
        this.totalPrice = calculateTotal(cart);
    }

    private BigDecimal calculateTotal(Cart cart) {
        return cart.getProducts().stream().map(cp -> cp.getUnitPrice().multiply(new BigDecimal(cp.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}

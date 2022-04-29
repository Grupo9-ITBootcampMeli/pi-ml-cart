package com.piml.cart.dto;

import lombok.*;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PriceDto {
    private BigDecimal price;
}

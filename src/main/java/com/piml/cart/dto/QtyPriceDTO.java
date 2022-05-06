package com.piml.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class QtyPriceDTO {
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal total;

}

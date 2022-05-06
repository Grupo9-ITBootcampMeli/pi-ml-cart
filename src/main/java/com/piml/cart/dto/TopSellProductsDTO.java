package com.piml.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class TopSellProductsDTO {
    private Long product_id;
    private String name;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal total;
}

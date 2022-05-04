package com.piml.cart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.piml.cart.dto.CartProductDto;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonIgnore
    private Cart cart;
    private Long productId;
    private Integer quantity;
    private BigDecimal unitPrice;

    public CartProduct(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public CartProductDto map() {
        return CartProductDto.builder()
                .productId(this.getProductId())
                .quantity(this.getQuantity()).build();
    }

    public Map.Entry<Long, Integer> mapQttyByProductId () {
        return Map.entry(this.getProductId(), this.getQuantity());
    }
}

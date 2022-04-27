package com.piml.cart.entity;

import lombok.*;

import javax.persistence.*;

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
    private Long cart_id;
//    incerto sobre a linha 17
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;
//    linha 21 conforme exemplo no playground
    private String product_id;
    private Integer quantity;
}

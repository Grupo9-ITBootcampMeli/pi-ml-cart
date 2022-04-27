package com.piml.cart.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;
    private Long buyerId;
    private String orderStatus;
    private LocalDateTime orderDate;

    @OneToMany(mappedBy = "cart")
    private Set<CartProduct> products;
}

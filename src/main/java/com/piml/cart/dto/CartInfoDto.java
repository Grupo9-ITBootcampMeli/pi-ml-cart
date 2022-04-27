package com.piml.cart.dto;

import com.piml.cart.util.OrderStatusENUM;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartInfoDto {

    private LocalDateTime date;
    private String buyerId;
    private String orderStatus;
}

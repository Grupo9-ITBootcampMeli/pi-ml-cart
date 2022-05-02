package com.piml.cart.dto;


import lombok.*;

import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WarehouseStockDto {
    private Long warehouseCode;
    private Long sectionCode;
    private Long productId;
    private List<BatchDto> batchStock;

    private Integer getTotalQtty () {
        return this.getBatchStock().stream().map(BatchDto::getCurrentQuantity).reduce(0, Integer::sum);
    }

    public Map.Entry<Long, Integer> mapQttyByProductId () {
        return Map.entry(this.getProductId(), this.getTotalQtty());
    }

}

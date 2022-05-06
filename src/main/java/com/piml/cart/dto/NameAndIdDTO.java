package com.piml.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class NameAndIdDTO {
    private String name;
    private Long id;

    @Override
    public String toString() {
        return "NameAndIdDTO{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}

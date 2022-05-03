package com.piml.cart.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyerDto {
    private String name;
    private String cpf;
    private String username;

    private String password;

    private String email;

    private final String role = "buyer";

    @Override
    public String toString() {
        return "BuyerDto{" +
                "name='" + name + '\'' +
                ", cpf='" + cpf + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}

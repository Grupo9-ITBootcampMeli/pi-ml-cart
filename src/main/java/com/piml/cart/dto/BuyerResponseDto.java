package com.piml.cart.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuyerResponseDto {
    private String name;
    private String cpf;
    private String username;
    private String email;
    private String role;

    public static BuyerResponseDto map(BuyerDto dto) {
        return BuyerResponseDto.builder()
                .name(dto.getName())
                .cpf(dto.getCpf())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .role(dto.getRole())
                .build();
    }
}

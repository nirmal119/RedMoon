package com.example.redmoon.dtos;

import lombok.Data;

@Data
public class ValidateTokenRequestDto {

    private String token;
    private Long userId;

}

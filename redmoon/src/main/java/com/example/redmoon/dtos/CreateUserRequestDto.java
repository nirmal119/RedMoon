package com.example.redmoon.dtos;

import lombok.Data;

@Data
public class CreateUserRequestDto {
    private String email;
    private String password;
    private String displayName;
}

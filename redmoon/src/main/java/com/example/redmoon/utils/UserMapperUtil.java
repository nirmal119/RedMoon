package com.example.redmoon.utils;

import com.example.redmoon.dtos.CreateUserRequestDto;
import com.example.redmoon.dtos.UserDto;
import com.example.redmoon.models.User;

public class UserMapperUtil {

    public static UserDto userDtoFromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getPhoneorEmail());
        return userDto;
    }

    public static User userFromCreateUserRequestDto(CreateUserRequestDto createUserRequestDto) {
        User user = new User();
        user.setPhoneorEmail(createUserRequestDto.getEmail());
        user.setPasswordHash(createUserRequestDto.getPassword());
        user.setDisplayName(createUserRequestDto.getDisplayName());
        return user;
    }
}

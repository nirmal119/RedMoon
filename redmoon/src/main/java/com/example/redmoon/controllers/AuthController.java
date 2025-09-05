package com.example.redmoon.controllers;

import com.example.redmoon.dtos.LoginRequestDto;
import com.example.redmoon.dtos.SignupRequestDto;
import com.example.redmoon.dtos.UserDto;
import com.example.redmoon.exceptions.PasswordMismatchException;
import com.example.redmoon.exceptions.UserAlreadySignedInException;
import com.example.redmoon.exceptions.UserNotFoundInSystemException;
import com.example.redmoon.models.User;
import com.example.redmoon.services.AuthService;
import com.example.redmoon.services.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthServiceImpl authService;

    @PostMapping("/signup")
    public UserDto signup(@RequestBody SignupRequestDto signupRequestDto) {
        try {
            User user = authService.signup(signupRequestDto.getEmail(), signupRequestDto.getPassword(), signupRequestDto.getDisplayName());
            return from(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto)  {
        try {
            ResponseEntity<User> userResponseEntity = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
            UserDto userDto = from(userResponseEntity.getBody());
            return new ResponseEntity<>(userDto, userResponseEntity.getHeaders(), userResponseEntity.getStatusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getPhoneorEmail());
        return userDto;
    }

}

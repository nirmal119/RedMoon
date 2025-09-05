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
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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
            Pair<User, String> userPair = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
            User user = userPair.getFirst();
            String token = userPair.getSecond();

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add(HttpHeaders.SET_COOKIE, token);

            UserDto userDto = from(user);
            return new ResponseEntity<>(userDto, headers, HttpStatusCode.valueOf(201));
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

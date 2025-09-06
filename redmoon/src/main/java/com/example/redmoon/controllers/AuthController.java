package com.example.redmoon.controllers;

import com.example.redmoon.dtos.LoginRequestDto;
import com.example.redmoon.dtos.SignupRequestDto;
import com.example.redmoon.dtos.UserDto;
import com.example.redmoon.dtos.ValidateTokenRequestDto;
import com.example.redmoon.models.User;
import com.example.redmoon.services.AuthServiceImpl;
import com.example.redmoon.utils.UserMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
            return UserMapperUtil.userDtoFromUser(user);
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

            UserDto userDto = UserMapperUtil.userDtoFromUser(user);
            return new ResponseEntity<>(userDto, headers, HttpStatusCode.valueOf(201));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @PostMapping("/validateToken")
    public ResponseEntity<String> validateToken(@RequestBody ValidateTokenRequestDto validateTokenRequestDto) {
        boolean result = authService.validateToken(validateTokenRequestDto.getToken(),
                validateTokenRequestDto.getUserId());
        if(result) {
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("FAILURE", HttpStatus.UNAUTHORIZED);
        }
    }

}

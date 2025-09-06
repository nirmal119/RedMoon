package com.example.redmoon.controllers;

import com.example.redmoon.dtos.CreateUserRequestDto;
import com.example.redmoon.dtos.UserDto;
import com.example.redmoon.models.User;
import com.example.redmoon.services.UserService;
import com.example.redmoon.utils.UserMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<UserDto> getUserById(@RequestParam(required = false, name = "id") Long userId,
                                               @RequestParam(required = false, name = "email") String email) {
        User user;

        if(userId != null) {
            user = userService.getUserById(userId);
        } else if (email != null) {
            user = userService.getUserByEmail(email);
        }else {
            throw new RuntimeException("Either id or email must be provided");
        }

        if(user == null) {
            throw new RuntimeException("User not found");
        }

        return new ResponseEntity<>(UserMapperUtil.userDtoFromUser(user), HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);

        if(user == null) {
            throw new RuntimeException("User not found");
        }

        return new ResponseEntity<>(UserMapperUtil.userDtoFromUser(user), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserRequestDto createUserRequestDto) {
        UserDto userDto = UserMapperUtil.userDtoFromUser(userService.createUser(UserMapperUtil.userFromCreateUserRequestDto(createUserRequestDto)));
        return new ResponseEntity<>(userDto, HttpStatusCode.valueOf(201));
    }

    @PatchMapping("/userId/{userId}/email/{newEmail}")
    public ResponseEntity<UserDto> updateUserEmail(@PathVariable Long userId,
                                                   @PathVariable String newEmail) {
        User user = userService.updateUserEmail(userId, newEmail);

        if(user == null) {
            throw new RuntimeException("User not found");
        }

        return new ResponseEntity<>(UserMapperUtil.userDtoFromUser(user), HttpStatusCode.valueOf(202));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable String email) {
        boolean deleted = userService.deleteUser(email);

        if(!deleted) {
            throw new RuntimeException("User not found");
        }

        return new ResponseEntity<>(true, HttpStatusCode.valueOf(202));
    }
}

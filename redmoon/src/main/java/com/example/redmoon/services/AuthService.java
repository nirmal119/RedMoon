package com.example.redmoon.services;

import com.example.redmoon.models.User;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    public User signup(String email, String password, String displayName);

    public ResponseEntity<User> login(String email, String password);
}

package com.example.redmoon.services;

import com.example.redmoon.models.User;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    public User signup(String email, String password, String displayName);

    public Pair<User,String> login(String email, String password);

    public boolean validateToken(String token, Long userId);
}

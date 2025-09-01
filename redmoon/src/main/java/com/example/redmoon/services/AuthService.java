package com.example.redmoon.services;

import com.example.redmoon.models.User;

public interface AuthService {

    public User signup(String email, String password, String displayName);

    public User login(String email, String password);
}

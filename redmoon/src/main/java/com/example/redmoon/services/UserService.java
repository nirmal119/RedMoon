package com.example.redmoon.services;

import com.example.redmoon.models.User;

public interface UserService {

    public User getUserById(Long userId);

    public User getUserByEmail(String email);

    public User createUser(User input);

    public User updateUserEmail(Long userId, String email);

    public Boolean deleteUser(String email);

}

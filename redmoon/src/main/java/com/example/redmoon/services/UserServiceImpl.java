package com.example.redmoon.services;

import com.example.redmoon.models.State;
import com.example.redmoon.models.User;
import com.example.redmoon.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public User getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByPhoneorEmail(email);
        return userOptional.orElse(null);
    }

    @Override
    public User createUser(User input) {
        return userRepository.save(input);
    }

    @Override
    public User updateUserEmail(Long userId,String email) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            return null;
        } else {
            User user = userOptional.get();
            user.setPhoneorEmail(email);
            return userRepository.save(user);
        }
    }

    @Override
    public Boolean deleteUser(String email) {
        Optional<User> userOptional = userRepository.findByPhoneorEmail(email);
        if(userOptional.isEmpty()) {
            return false;
        } else {
            User user = userOptional.get();
            if(user.getState().equals((State.ACTIVE))) {
                user.setState(State.INACTIVE);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}

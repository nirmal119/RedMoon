package com.example.redmoon.services;

import com.example.redmoon.exceptions.PasswordMismatchException;
import com.example.redmoon.exceptions.UserAlreadySignedInException;
import com.example.redmoon.exceptions.UserNotFoundInSystemException;
import com.example.redmoon.models.User;
import com.example.redmoon.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordencoder;

    public User signup(String email, String password, String displayName) throws UserAlreadySignedInException{

        Optional<User> userOptional = userRepository.findByPhoneorEmail(email);
        if(userOptional.isPresent()) {
            throw new UserAlreadySignedInException("User already signed in, Please login!");
        }

        User user = new User();
        user.setPhoneorEmail(email);
//        user.setPasswordHash(password);
        user.setPasswordHash(bCryptPasswordencoder.encode(password));
        user.setDisplayName(displayName);
        userRepository.save(user);

        return user;
    }

    public User login(String email, String password) throws PasswordMismatchException, UserNotFoundInSystemException{
        Optional<User> userOptional = userRepository.findByPhoneorEmail(email);
        if(userOptional.isEmpty()) {
            throw new UserNotFoundInSystemException("User has not signed up.");
        }
        User user =  userOptional.get();
        String  passwordHash = user.getPasswordHash();
//        if(!passwordHash.equals(password)) {
        if(!bCryptPasswordencoder.matches(password, passwordHash)){
            throw new PasswordMismatchException("Incorrect password!");
        }
        return user;
    }
}

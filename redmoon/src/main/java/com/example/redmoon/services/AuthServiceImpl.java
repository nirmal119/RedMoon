package com.example.redmoon.services;

import com.example.redmoon.exceptions.PasswordMismatchException;
import com.example.redmoon.exceptions.UserAlreadySignedInException;
import com.example.redmoon.exceptions.UserNotFoundInSystemException;
import com.example.redmoon.models.User;
import com.example.redmoon.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
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

    public ResponseEntity<User> login(String email, String password) throws PasswordMismatchException, UserNotFoundInSystemException{
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

        /* Token generation logic */

//        byte[] content = message.getBytes(StandardCharsets.UTF_8);

        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",user.getId());
        Long nowInMillis = System.currentTimeMillis();
        claims.put("iat",nowInMillis); // issued at
        claims.put("exp",nowInMillis+100000);
        claims.put("iss","RedMoon"); // issuer


        MacAlgorithm algorithm = Jwts.SIG.HS256;
        SecretKey secretKey = algorithm.key().build();

        String token = Jwts.builder().claims(claims).signWith(secretKey).compact();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE, token);
        ResponseEntity<User> userResponseEntity = new ResponseEntity<>(user,
                headers,
                HttpStatusCode.valueOf(201));

        return userResponseEntity;
    }
}

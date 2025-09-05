package com.example.redmoon.services;

import com.example.redmoon.exceptions.PasswordMismatchException;
import com.example.redmoon.exceptions.UserAlreadySignedInException;
import com.example.redmoon.exceptions.UserNotFoundInSystemException;
import com.example.redmoon.models.User;
import com.example.redmoon.models.UserSession;
import com.example.redmoon.repositories.UserRepository;
import com.example.redmoon.repositories.UserSessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    UserSessionRepository userSessionRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordencoder;
    @Autowired
    private SecretKey secretKey;

    public User signup(String email, String password, String displayName) throws UserAlreadySignedInException{

        Optional<User> userOptional = userRepository.findByPhoneorEmail(email);
        if(userOptional.isPresent()) {
            throw new UserAlreadySignedInException("User already signed in, Please login!");
        }

        User user = new User();
        user.setPhoneorEmail(email);
        user.setPasswordHash(bCryptPasswordencoder.encode(password));
        user.setDisplayName(displayName);
        userRepository.save(user);

        return user;
    }

    public Pair<User,String> login(String email, String password) throws PasswordMismatchException, UserNotFoundInSystemException{
        Optional<User> userOptional = userRepository.findByPhoneorEmail(email);
        if(userOptional.isEmpty()) {
            throw new UserNotFoundInSystemException("User has not signed up.");
        }
        User user =  userOptional.get();
        String  passwordHash = user.getPasswordHash();

        if(!bCryptPasswordencoder.matches(password, passwordHash)){
            throw new PasswordMismatchException("Incorrect password!");
        }

        /* Token generation logic */
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",user.getId());
        Long nowInMillis = System.currentTimeMillis();
        claims.put("iat",nowInMillis); // issued at
        claims.put("exp",nowInMillis+100000);
        claims.put("iss","RedMoon"); // issuer

        String token = Jwts.builder().claims(claims).signWith(secretKey).compact();

        /* Persisting generated token */
        UserSession userSession = new UserSession();
        userSession.setUser(user);
        userSession.setToken(token);
        userSessionRepository.save(userSession);

        return Pair.of(user,token);
    }

    public boolean validateToken(String token, Long userId) {
        Optional<UserSession> optionalUserSession = userSessionRepository.findByTokenAndUser_Id(token,userId);
        if(optionalUserSession.isEmpty()) return false;
        UserSession userSession = optionalUserSession.get();

        /* Parsing token */
        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();
        Long expiry = (Long)claims.get("exp");
        Long currentTime = System.currentTimeMillis();

        return currentTime <= expiry;
    }
}

package com.example.sweetshop.auth.service;

import com.example.sweetshop.auth.repository.UserRepository;
import com.example.sweetshop.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final Key SECRET_KEY;
    private final long EXPIRATION_TIME;

    @Autowired
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationTime
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes());
        this.EXPIRATION_TIME = expirationTime;
    }

    // --------------------------
    // Registration
    // --------------------------
    public User registerUser(User user) throws Exception {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new Exception("Username is already taken");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // --------------------------
    // Authenticate user credentials
    // --------------------------
    public Optional<User> authenticate(String username, String rawPassword) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && passwordEncoder.matches(rawPassword, userOpt.get().getPassword())) {
            return userOpt;
        }
        return Optional.empty();
    }

    // --------------------------
    // Generate JWT Token
    // --------------------------
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    // --------------------------
    // Login method returning JWT
    // --------------------------
    public String login(String username, String password) throws Exception {
        Optional<User> userOpt = authenticate(username, password);
        if (userOpt.isEmpty()) {
            throw new Exception("Invalid username or password");
        }
        return generateToken(userOpt.get());
    }

    // --------------------------
    // Get user by username
    // --------------------------
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}

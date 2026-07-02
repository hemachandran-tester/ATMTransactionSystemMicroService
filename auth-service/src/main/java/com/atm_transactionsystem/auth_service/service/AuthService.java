package com.atm_transactionsystem.auth_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.atm_transactionsystem.auth_service.dto.AuthResponse;
import com.atm_transactionsystem.auth_service.dto.LoginRequest;
import com.atm_transactionsystem.auth_service.dto.RegisterRequest;
import com.atm_transactionsystem.auth_service.entity.User;
import com.atm_transactionsystem.auth_service.exception.InvalidCredentialsException;
import com.atm_transactionsystem.auth_service.exception.UserAlreadyExistsException;
import com.atm_transactionsystem.auth_service.repository.UserRepository;
import com.atm_transactionsystem.auth_service.util.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Register a new user
    public String register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);

        return "User Registered Successfully";
    }

    // Login user
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid Username"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid Password");
        }

        String token = jwtUtil.generateToken(user.getUsername());

        return new AuthResponse(token);
    }
}
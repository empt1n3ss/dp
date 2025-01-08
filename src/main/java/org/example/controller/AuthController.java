package org.example.controller;

import org.example.dto.LoginRequest;
import org.example.dto.LoginResponse;
import org.example.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/cloud")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        logger.info("Login attempt with username: {}", request.getLogin());
        String authToken = authService.login(request.getLogin(), request.getPassword());
        logger.info("Login successful for username: {}", request.getLogin());
        return ResponseEntity.ok(new LoginResponse(authToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("auth-token") String authToken) {
        logger.info("Logout attempt with auth-token: {}", authToken);
        authService.logout(authToken);
        logger.info("Logout successful for auth-token: {}", authToken);
        return ResponseEntity.ok().build();
    }
}

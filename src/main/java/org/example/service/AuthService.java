package org.example.service;

import org.example.entity.UserEntity;
import org.example.exception.UnauthorizedException;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String login, String password) {
        logger.info("Login attempt with username: {}", login);
        Optional<UserEntity> userOptional = userRepository.findByLogin(login);

        if (userOptional.isEmpty() || !passwordEncoder.matches(password, userOptional.get().getPassword())) {
            logger.warn("Invalid login or password for username: {}", login);
            throw new UnauthorizedException("Invalid login or password");
        }

        UserEntity user = userOptional.get();
        String token = UUID.randomUUID().toString();
        user.setAuthToken(token);
        userRepository.save(user);

        logger.info("Login successful for username: {}", login);
        return token;
    }

    public void logout(String authToken) {
        logger.info("Logout attempt with auth-token: {}", authToken);
        Optional<UserEntity> userOptional = userRepository.findByAuthToken(authToken);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.setAuthToken(null);
            userRepository.save(user);
            logger.info("Logout successful for auth-token: {}", authToken);
        } else {
            logger.warn("Invalid token: {}", authToken);
            throw new UnauthorizedException("Invalid token");
        }
    }

    public boolean isAuthenticated(String authToken) {
        boolean authenticated = userRepository.findByAuthToken(authToken).isPresent();
        logger.info("Authentication check for token: {}: {}", authToken, authenticated);
        return authenticated;
    }
}

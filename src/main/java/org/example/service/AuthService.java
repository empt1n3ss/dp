package org.example.service;

import org.example.entity.UserEntity;
import org.example.exception.UnauthorizedException;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String login, String password) {
        Optional<UserEntity> userOptional = userRepository.findByLogin(login);

        if (userOptional.isEmpty() || !passwordEncoder.matches(password, userOptional.get().getPassword())) {
            throw new UnauthorizedException("Invalid login or password");
        }

        UserEntity user = userOptional.get();
        String token = UUID.randomUUID().toString();
        user.setAuthToken(token);
        userRepository.save(user);

        return token;
    }

    public void logout(String authToken) {
        Optional<UserEntity> userOptional = userRepository.findByAuthToken(authToken);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.setAuthToken(null);
            userRepository.save(user);
        } else {
            throw new UnauthorizedException("Invalid token");
        }
    }

    public boolean isAuthenticated(String authToken) {
        return userRepository.findByAuthToken(authToken).isPresent();
    }
}

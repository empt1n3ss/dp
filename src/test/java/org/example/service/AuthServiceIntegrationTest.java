package org.example.service;

import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void testLoginAndLogout() {
        String login = "testuser";
        String password = "password";
        String encodedPassword = passwordEncoder.encode(password);

        UserEntity user = new UserEntity();
        user.setLogin(login);
        user.setPassword(encodedPassword);

        userRepository.save(user);

        String token = authService.login(login, password);
        assertNotNull(token);

        authService.logout(token);
        assertNull(user.getAuthToken());
    }
}

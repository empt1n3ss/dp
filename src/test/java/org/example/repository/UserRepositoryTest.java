package org.example.service;

import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void testCreateUser() {
        String encodedPassword = passwordEncoder.encode("testpassword");
        UserEntity testUser = new UserEntity();
        testUser.setLogin("testuser");
        testUser.setPassword(encodedPassword);
        userRepository.save(testUser);

        UserEntity savedUser = userRepository.findByLogin("testuser").orElse(null);
        assertNotNull(savedUser);
        System.out.println("User created with ID: " + savedUser.getId());
    }
}

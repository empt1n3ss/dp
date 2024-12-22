package org.example.entity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserEntityTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "testpassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println("Encoded password: " + encodedPassword);
    }
}

package org.example.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginRequestTest {

    @Test
    void testLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setLogin("testuser");
        request.setPassword("password");

        assertEquals("testuser", request.getLogin());
        assertEquals("password", request.getPassword());

        request.setLogin("newuser");
        request.setPassword("newpassword");

        assertEquals("newuser", request.getLogin());
        assertEquals("newpassword", request.getPassword());
    }
}

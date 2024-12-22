package org.example.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginResponseTest {

    @Test
    void testLoginResponse() {
        LoginResponse response = new LoginResponse("auth-token");

        assertEquals("auth-token", response.getAuthToken());

        response.setAuthToken("new-auth-token");

        assertEquals("new-auth-token", response.getAuthToken());
    }
}

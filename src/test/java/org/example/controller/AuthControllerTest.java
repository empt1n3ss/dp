package org.example.controller;

import org.example.dto.LoginRequest;
import org.example.dto.LoginResponse;
import org.example.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private AuthService authService;
    private AuthController authController;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        authController = new AuthController(authService);
    }

    @Test
    void login_shouldReturnAuthToken() {
        LoginRequest request = new LoginRequest();
        request.setLogin("testuser");
        request.setPassword("testpassword");

        String expectedToken = "valid-token";
        when(authService.login(request.getLogin(), request.getPassword())).thenReturn(expectedToken);

        ResponseEntity<LoginResponse> response = authController.login(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedToken, response.getBody().getAuthToken());
        verify(authService, times(1)).login(request.getLogin(), request.getPassword());
    }

    @Test
    void logout_shouldCallLogoutService() {
        String authToken = "valid-token";

        ResponseEntity<Void> response = authController.logout(authToken);

        assertEquals(200, response.getStatusCodeValue());
        verify(authService, times(1)).logout(authToken);
    }
}

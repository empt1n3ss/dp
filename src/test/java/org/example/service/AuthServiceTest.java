package org.example.service;

import org.example.entity.UserEntity;
import org.example.exception.UnauthorizedException;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreValid() {
        String login = "testuser";
        String password = "password";
        String encodedPassword = "encodedPassword";
        UserEntity user = new UserEntity();
        user.setLogin(login);
        user.setPassword(encodedPassword);

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        String token = authService.login(login, password);

        assertNotNull(token);
        verify(userRepository).save(user);
    }

    @Test
    void login_ShouldThrowUnauthorizedException_WhenCredentialsAreInvalid() {
        String login = "testuser";
        String password = "wrongpassword";

        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> authService.login(login, password));
        verify(userRepository, never()).save(any());
    }

    @Test
    void logout_ShouldClearAuthToken_WhenTokenIsValid() {
        String authToken = UUID.randomUUID().toString();
        UserEntity user = new UserEntity();
        user.setAuthToken(authToken);

        when(userRepository.findByAuthToken(authToken)).thenReturn(Optional.of(user));

        authService.logout(authToken);

        assertNull(user.getAuthToken());
        verify(userRepository).save(user);
    }

    @Test
    void logout_ShouldThrowUnauthorizedException_WhenTokenIsInvalid() {
        String authToken = "invalidToken";

        when(userRepository.findByAuthToken(authToken)).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> authService.logout(authToken));
        verify(userRepository, never()).save(any());
    }

    @Test
    void isAuthenticated_ShouldReturnTrue_WhenTokenIsValid() {
        String authToken = "validToken";
        when(userRepository.findByAuthToken(authToken)).thenReturn(Optional.of(new UserEntity()));

        boolean isAuthenticated = authService.isAuthenticated(authToken);

        assertTrue(isAuthenticated);
    }

    @Test
    void isAuthenticated_ShouldReturnFalse_WhenTokenIsInvalid() {
        String authToken = "invalidToken";
        when(userRepository.findByAuthToken(authToken)).thenReturn(Optional.empty());

        boolean isAuthenticated = authService.isAuthenticated(authToken);

        assertFalse(isAuthenticated);
    }
}

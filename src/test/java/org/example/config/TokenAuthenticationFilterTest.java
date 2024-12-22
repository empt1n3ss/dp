package org.example.config;

import org.example.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TokenAuthenticationFilterTest {

    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Mock
    private AuthService authService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        tokenAuthenticationFilter = new TokenAuthenticationFilter(authService);
    }

    @Test
    public void testFilterInitialization() {
        assertNotNull(tokenAuthenticationFilter, "TokenAuthenticationFilter should be initialized");
    }
}

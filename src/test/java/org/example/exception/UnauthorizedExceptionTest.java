package org.example.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnauthorizedExceptionTest {

    @Test
    void testUnauthorizedException() {
        UnauthorizedException exception = new UnauthorizedException("Unauthorized access");

        assertEquals("Unauthorized access", exception.getMessage());
    }
}

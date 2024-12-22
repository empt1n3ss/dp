package org.example.exception;

import org.example.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    @Test
    void testHandleUnauthorizedException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        UnauthorizedException ex = new UnauthorizedException("Unauthorized access");
        ResponseEntity<ErrorResponse> response = handler.handleUnauthorizedException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unauthorized access", response.getBody().getMessage());
    }
}

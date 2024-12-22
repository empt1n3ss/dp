package org.example.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorResponseTest {

    @Test
    void testErrorResponseConstructorAndGetters() {
        String message = "Error occurred";
        int code = 400;

        ErrorResponse errorResponse = new ErrorResponse(message, code);

        assertEquals(message, errorResponse.getMessage());
        assertEquals(code, errorResponse.getCode());
    }
}

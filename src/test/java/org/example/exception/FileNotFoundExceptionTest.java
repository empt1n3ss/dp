package org.example.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileNotFoundExceptionTest {

    @Test
    void testFileNotFoundException() {
        FileNotFoundException exception = new FileNotFoundException("File not found");

        assertEquals("File not found", exception.getMessage());
    }
}

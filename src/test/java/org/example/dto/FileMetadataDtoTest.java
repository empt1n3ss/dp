package org.example.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileMetadataDtoTest {

    @Test
    void testFileMetadataDtoConstructorAndGetters() {
        String expectedFilename = "example.txt";
        long expectedSize = 1024L;

        FileMetadataDto fileMetadataDto = new FileMetadataDto(expectedFilename, expectedSize);

        assertEquals(expectedFilename, fileMetadataDto.getFilename());
        assertEquals(expectedSize, fileMetadataDto.getSize());
    }
}

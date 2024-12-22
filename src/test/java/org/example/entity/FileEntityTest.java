package org.example.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileEntityTest {

    @Test
    void testFileEntitySettersAndGetters() {
        String expectedName = "example.txt";
        byte[] expectedContent = "file content".getBytes();

        FileEntity fileEntity = new FileEntity();

        fileEntity.setName(expectedName);
        fileEntity.setContent(expectedContent);

        assertEquals(expectedName, fileEntity.getName());
        assertArrayEquals(expectedContent, fileEntity.getContent());
    }
}

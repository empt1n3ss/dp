package org.example.service;

import org.example.entity.FileEntity;
import org.example.repository.FileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileServiceIntegrationTest {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileRepository fileRepository;

    @Test
    void testUploadFile() throws Exception {
        MultipartFile file = new MockMultipartFile("testFile", "test.txt", "text/plain", "test content".getBytes());

        fileService.uploadFile("test.txt", file);

        FileEntity savedFile = fileRepository.findByName("test.txt").orElse(null);
        assertNotNull(savedFile);
        assertEquals("test.txt", savedFile.getName());
    }
}

package org.example.service;

import org.example.entity.FileEntity;
import org.example.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FileServiceTest {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileRepository fileRepository;

    @BeforeEach
    void setUp() {
        fileRepository.deleteAll();
    }

    @Test
    void downloadFile_ShouldReturnResource() throws Exception {
        FileEntity file = new FileEntity();
        file.setName("test.txt");
        file.setContent("Test content".getBytes());
        fileRepository.save(file);

        Resource resource = fileService.downloadFile("test.txt");
        assertNotNull(resource);
        assertEquals("test.txt", resource.getFilename());
    }

    @Test
    void renameFile_ShouldRenameFile() throws Exception {
        FileEntity file = new FileEntity();
        file.setName("test.txt");
        file.setContent("Test content".getBytes());
        fileRepository.save(file);

        fileService.renameFile("test.txt", "newName.txt");

        FileEntity renamedFile = fileRepository.findByName("newName.txt").orElseThrow();
        assertEquals("newName.txt", renamedFile.getName());
    }

    @Test
    void deleteFile_ShouldDeleteFile() throws Exception {
        FileEntity file = new FileEntity();
        file.setName("test.txt");
        file.setContent("Test content".getBytes());
        fileRepository.save(file);

        fileService.deleteFile("test.txt");

        assertFalse(fileRepository.existsByName("test.txt"));
    }
}

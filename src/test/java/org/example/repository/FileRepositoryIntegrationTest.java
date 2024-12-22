package org.example.repository;

import org.example.entity.FileEntity;
import org.example.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class FileRepositoryIntegrationTest {

    @Autowired
    private FileRepository fileRepository;

    private FileEntity fileEntity;

    @BeforeEach
    void setUp() {
        fileEntity = new FileEntity();
        fileEntity.setName("testfile.txt");
        fileEntity.setContent(new byte[]{1, 2, 3});
    }

    @Test
    void testSaveFileEntity() {
        FileEntity savedFile = fileRepository.save(fileEntity);
        assertNotNull(savedFile.getName(), "File name should not be null");
    }

    @Test
    void testFindByName() {
        fileRepository.save(fileEntity);
        FileEntity foundFile = fileRepository.findByName("testfile.txt").orElse(null);
        assertNotNull(foundFile, "File should be found by name");
        assertTrue(foundFile.getName().equals("testfile.txt"));
    }
}

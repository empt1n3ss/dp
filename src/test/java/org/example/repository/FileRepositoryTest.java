package org.example.repository;

import org.example.entity.FileEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class FileRepositoryTest {

    @Autowired
    private FileRepository fileRepository;

    @Test
    public void testFindByName() {
        Optional<FileEntity> file = fileRepository.findByName("someFileName");
        assertTrue(file.isPresent(), "File should be present");
    }
}

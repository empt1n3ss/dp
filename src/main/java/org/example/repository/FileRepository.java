package org.example.repository;

import org.example.entity.FileEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FileRepository extends CrudRepository<FileEntity, Long> {
    Optional<FileEntity> findByName(String name);
    boolean existsByName(String name);
}

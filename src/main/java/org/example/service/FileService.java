package org.example.service;

import org.example.dto.FileMetadataDto;
import org.example.exception.FileNotFoundException;
import org.example.exception.UnauthorizedException;
import org.example.entity.FileEntity;
import org.example.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    private final String storagePath = "storage/";
    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;

        File directory = new File(storagePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void uploadFile(String authToken, String filename, MultipartFile file) {
        if (!isAuthorized(authToken)) {
            throw new UnauthorizedException("Unauthorized");
        }

        try {
            File targetFile = new File(storagePath + filename);
            file.transferTo(targetFile);

            FileEntity fileEntity = new FileEntity();
            fileEntity.setName(filename);
            fileEntity.setContent(file.getBytes());

            fileRepository.save(fileEntity);
        } catch (IOException e) {
            throw new RuntimeException("Error saving file: " + e.getMessage(), e);
        }
    }

    public void deleteFile(String authToken, String filename) {
        if (!isAuthorized(authToken)) {
            throw new UnauthorizedException("Unauthorized");
        }

        File targetFile = new File(storagePath + filename);
        if (!targetFile.exists()) {
            throw new FileNotFoundException("File not found: " + filename);
        }

        if (!targetFile.delete()) {
            throw new RuntimeException("Error deleting file: " + filename);
        }

        Optional<FileEntity> fileEntityOpt = fileRepository.findByName(filename);
        if (fileEntityOpt.isPresent()) {
            fileRepository.delete(fileEntityOpt.get());
        } else {
            throw new FileNotFoundException("File metadata not found in database: " + filename);
        }
    }

    public Resource downloadFile(String authToken, String filename) {
        if (!isAuthorized(authToken)) {
            throw new UnauthorizedException("Unauthorized");
        }

        File targetFile = new File(storagePath + filename);
        if (!targetFile.exists()) {
            throw new FileNotFoundException("File not found: " + filename);
        }

        return new FileSystemResource(targetFile);
    }

    public void renameFile(String authToken, String filename, String newName) {
        if (!isAuthorized(authToken)) {
            throw new UnauthorizedException("Unauthorized");
        }

        File targetFile = new File(storagePath + filename);
        if (!targetFile.exists()) {
            throw new FileNotFoundException("File not found: " + filename);
        }

        File newFile = new File(storagePath + newName);
        if (!targetFile.renameTo(newFile)) {
            throw new RuntimeException("Error renaming file");
        }

        Optional<FileEntity> fileEntityOpt = fileRepository.findByName(filename);
        if (fileEntityOpt.isPresent()) {
            FileEntity fileEntity = fileEntityOpt.get();
            fileEntity.setName(newName);
            fileRepository.save(fileEntity);
        }
    }

    public List<FileMetadataDto> getAllFiles(String authToken, int limit) {
        if (!isAuthorized(authToken)) {
            throw new UnauthorizedException("Unauthorized");
        }

        File folder = new File(storagePath);
        File[] files = folder.listFiles();

        if (files == null) {
            return new ArrayList<>();
        }

        List<FileMetadataDto> metadataList = new ArrayList<>();
        for (int i = 0; i < Math.min(files.length, limit); i++) {
            metadataList.add(new FileMetadataDto(files[i].getName(), files[i].length()));
        }
        return metadataList;
    }

    private boolean isAuthorized(String authToken) {
        return "valid-token".equals(authToken);
    }
}

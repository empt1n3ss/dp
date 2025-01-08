package org.example.service;

import org.example.dto.FileMetadataDto;
import org.example.entity.FileEntity;
import org.example.exception.FileNotFoundException;
import org.example.exception.FileOperationException;
import org.example.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    private final String storagePath = "storage/";
    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;

        File directory = new File(storagePath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new FileOperationException("Failed to create storage directory");
            }
        }
    }

    public void uploadFile(String filename, MultipartFile file) {
        logger.info("Uploading file: {}", filename);
        try {
            File targetFile = new File(storagePath + filename);
            file.transferTo(targetFile);

            FileEntity fileEntity = new FileEntity();
            fileEntity.setName(filename);
            fileEntity.setContent(file.getBytes());

            fileRepository.save(fileEntity);
            logger.info("File uploaded successfully: {}", filename);
        } catch (IOException e) {
            logger.error("Error saving file: {}", filename, e);
            throw new FileOperationException("Error saving file: " + filename, e);
        }
    }

    public void deleteFile(String filename) {
        logger.info("Deleting file: {}", filename);
        File targetFile = new File(storagePath + filename);
        if (!targetFile.exists()) {
            logger.warn("File not found: {}", filename);
            throw new FileNotFoundException("File not found: " + filename);
        }

        if (!targetFile.delete()) {
            logger.error("Error deleting file: {}", filename);
            throw new FileOperationException("Error deleting file: " + filename);
        }

        Optional<FileEntity> fileEntityOpt = fileRepository.findByName(filename);
        if (fileEntityOpt.isPresent()) {
            fileRepository.delete(fileEntityOpt.get());
        } else {
            logger.error("File metadata not found in database for file: {}", filename);
            throw new FileOperationException("File metadata not found in database: " + filename);
        }
        logger.info("File deleted successfully: {}", filename);
    }

    public Resource downloadFile(String filename) {
        logger.info("Downloading file: {}", filename);
        File targetFile = new File(storagePath + filename);
        if (!targetFile.exists()) {
            logger.warn("File not found: {}", filename);
            throw new FileNotFoundException("File not found: " + filename);
        }

        return new FileSystemResource(targetFile);
    }

    public void renameFile(String filename, String newName) {
        logger.info("Renaming file: {} to {}", filename, newName);
        File targetFile = new File(storagePath + filename);
        if (!targetFile.exists()) {
            logger.warn("File not found: {}", filename);
            throw new FileNotFoundException("File not found: " + filename);
        }

        File newFile = new File(storagePath + newName);
        if (!targetFile.renameTo(newFile)) {
            logger.error("Error renaming file from {} to {}", filename, newName);
            throw new FileOperationException("Error renaming file from " + filename + " to " + newName);
        }

        Optional<FileEntity> fileEntityOpt = fileRepository.findByName(filename);
        if (fileEntityOpt.isPresent()) {
            FileEntity fileEntity = fileEntityOpt.get();
            fileEntity.setName(newName);
            fileRepository.save(fileEntity);
        } else {
            logger.error("File metadata not found for file: {}", filename);
            throw new FileOperationException("File metadata not found for file: " + filename);
        }
        logger.info("File renamed successfully from {} to {}", filename, newName);
    }

    public List<FileMetadataDto> getAllFiles(int limit) {
        logger.info("Fetching file list with limit: {}", limit);
        File folder = new File(storagePath);
        File[] files = folder.listFiles();

        if (files == null) {
            logger.error("Error accessing storage directory");
            throw new FileOperationException("Error accessing storage directory");
        }

        List<FileMetadataDto> fileMetadataList = Arrays.stream(files)
                .limit(limit)
                .map(file -> new FileMetadataDto(file.getName(), file.length()))
                .toList();

        logger.info("Fetched file list with {} files", fileMetadataList.size());
        return fileMetadataList;
    }
}

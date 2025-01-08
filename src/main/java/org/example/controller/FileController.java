package org.example.controller;

import org.example.dto.FileMetadataDto;
import org.example.dto.EditFileRequest;
import org.example.exception.UnauthorizedException;
import org.example.service.AuthService;
import org.example.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/cloud")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final FileService fileService;
    private final AuthService authService;

    public FileController(FileService fileService, AuthService authService) {
        this.fileService = fileService;
        this.authService = authService;
    }

    private void checkAuthorization(String authToken) {
        if (!authService.isAuthenticated(authToken)) {
            logger.warn("Unauthorized access attempt with auth-token: {}", authToken);
            throw new UnauthorizedException("Unauthorized");
        }
    }

    @PostMapping("/file")
    public ResponseEntity<Void> uploadFile(@RequestHeader("auth-token") String authToken,
                                           @RequestParam("filename") String filename,
                                           @RequestParam("file") MultipartFile file) {
        logger.info("Upload attempt for file: {} with auth-token: {}", filename, authToken);
        checkAuthorization(authToken);
        fileService.uploadFile(filename, file);
        logger.info("File uploaded successfully: {}", filename);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/file")
    public ResponseEntity<Void> deleteFile(@RequestHeader("auth-token") String authToken,
                                           @RequestParam("filename") String filename) {
        logger.info("Delete attempt for file: {} with auth-token: {}", filename, authToken);
        checkAuthorization(authToken);
        fileService.deleteFile(filename);
        logger.info("File deleted successfully: {}", filename);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/file")
    public ResponseEntity<Resource> downloadFile(@RequestHeader("auth-token") String authToken,
                                                 @RequestParam("filename") String filename) {
        logger.info("Download attempt for file: {} with auth-token: {}", filename, authToken);
        checkAuthorization(authToken);
        var file = fileService.downloadFile(filename);
        logger.info("File downloaded successfully: {}", filename);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }

    @PutMapping("/file")
    public ResponseEntity<Void> editFileName(@RequestHeader("auth-token") String authToken,
                                             @RequestParam("filename") String filename,
                                             @RequestBody EditFileRequest request) {
        logger.info("Edit attempt for file: {} with new name: {} and auth-token: {}", filename, request.getName(), authToken);
        checkAuthorization(authToken);
        fileService.renameFile(filename, request.getName());
        logger.info("File renamed successfully: {} to {}", filename, request.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileMetadataDto>> getFiles(@RequestHeader("auth-token") String authToken,
                                                          @RequestParam("limit") int limit) {
        logger.info("Fetching file list with auth-token: {} and limit: {}", authToken, limit);
        checkAuthorization(authToken);
        List<FileMetadataDto> files = fileService.getAllFiles(limit);
        logger.info("File list fetched successfully, number of files: {}", files.size());
        return ResponseEntity.ok(files);
    }
}

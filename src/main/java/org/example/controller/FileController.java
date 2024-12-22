package org.example.controller;

import org.example.dto.FileMetadataDto;
import org.example.dto.EditFileRequest;
import org.example.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/cloud")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/file")
    public ResponseEntity<Void> uploadFile(@RequestHeader("auth-token") String authToken,
                                           @RequestParam("filename") String filename,
                                           @RequestParam("file") MultipartFile file) {
        fileService.uploadFile(authToken, filename, file);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/file")
    public ResponseEntity<Void> deleteFile(@RequestHeader("auth-token") String authToken,
                                           @RequestParam("filename") String filename) {
        fileService.deleteFile(authToken, filename);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/file")
    public ResponseEntity<Resource> downloadFile(@RequestHeader("auth-token") String authToken,
                                                 @RequestParam("filename") String filename) {
        var file = fileService.downloadFile(authToken, filename);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }

    @PutMapping("/file")
    public ResponseEntity<Void> editFileName(@RequestHeader("auth-token") String authToken,
                                             @RequestParam("filename") String filename,
                                             @RequestBody EditFileRequest request) {
        fileService.renameFile(authToken, filename, request.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileMetadataDto>> getFiles(@RequestHeader("auth-token") String authToken,
                                                          @RequestParam("limit") int limit) {
        List<FileMetadataDto> files = fileService.getAllFiles(authToken, limit);
        return ResponseEntity.ok(files);
    }
}

package org.example.controller;

import org.example.dto.EditFileRequest;
import org.example.dto.FileMetadataDto;
import org.example.service.AuthService;
import org.example.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FileControllerTest {

    private FileService fileService;
    private AuthService authService;
    private FileController fileController;

    @BeforeEach
    void setUp() {
        fileService = mock(FileService.class);
        authService = mock(AuthService.class);
        fileController = new FileController(fileService, authService);
    }

    private void mockAuthorization(String authToken, boolean isAuthenticated) {
        when(authService.isAuthenticated(authToken)).thenReturn(isAuthenticated);
    }

    @Test
    void uploadFile_shouldCallService() {
        String authToken = "valid-token";
        String filename = "test.txt";
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes());

        mockAuthorization(authToken, true);

        ResponseEntity<Void> response = fileController.uploadFile(authToken, filename, file);

        assertEquals(200, response.getStatusCodeValue());
        verify(authService, times(1)).isAuthenticated(authToken);
        verify(fileService, times(1)).uploadFile(filename, file);
    }

    @Test
    void deleteFile_shouldCallService() {
        String authToken = "valid-token";
        String filename = "test.txt";

        mockAuthorization(authToken, true);

        ResponseEntity<Void> response = fileController.deleteFile(authToken, filename);

        assertEquals(200, response.getStatusCodeValue());
        verify(authService, times(1)).isAuthenticated(authToken);
        verify(fileService, times(1)).deleteFile(filename);
    }

    @Test
    void downloadFile_shouldReturnFile() {
        String authToken = "valid-token";
        String filename = "test.txt";
        Resource mockResource = mock(Resource.class);

        mockAuthorization(authToken, true);
        when(fileService.downloadFile(filename)).thenReturn(mockResource);

        ResponseEntity<Resource> response = fileController.downloadFile(authToken, filename);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResource, response.getBody());
        verify(authService, times(1)).isAuthenticated(authToken);
        verify(fileService, times(1)).downloadFile(filename);
    }

    @Test
    void editFileName_shouldCallService() {
        String authToken = "valid-token";
        String filename = "old_name.txt";
        EditFileRequest request = new EditFileRequest("new_name.txt");

        mockAuthorization(authToken, true);

        ResponseEntity<Void> response = fileController.editFileName(authToken, filename, request);

        assertEquals(200, response.getStatusCodeValue());
        verify(authService, times(1)).isAuthenticated(authToken);
        verify(fileService, times(1)).renameFile(filename, request.getName());
    }

    @Test
    void getFiles_shouldReturnFileList() {
        String authToken = "valid-token";
        int limit = 10;
        List<FileMetadataDto> mockFileList = List.of(new FileMetadataDto("test.txt", 12345L));

        mockAuthorization(authToken, true);
        when(fileService.getAllFiles(limit)).thenReturn(mockFileList);

        ResponseEntity<List<FileMetadataDto>> response = fileController.getFiles(authToken, limit);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockFileList, response.getBody());
        verify(authService, times(1)).isAuthenticated(authToken);
        verify(fileService, times(1)).getAllFiles(limit);
    }
}

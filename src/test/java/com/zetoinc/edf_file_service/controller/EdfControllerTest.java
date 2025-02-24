package com.zetoinc.edf_file_service.controller;

import com.zetoinc.edf_file_service.exception.FileProcessingException;
import com.zetoinc.edf_file_service.exception.InvalidFileURLException;
import com.zetoinc.edf_file_service.model.EdfMetadata;
import com.zetoinc.edf_file_service.security.AuthService;
import com.zetoinc.edf_file_service.service.EdfFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
class EdfControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private EdfFileService edfFileService;

    @InjectMocks
    private EdfController edfController;

    private final String validKey = "very secret";
    private final String invalidKey = "wrong key";
    private final String validUrl = "https://example.com/sample.edf";
    private final String invalidUrl = "invalid-url";

    private EdfMetadata mockMetadata;

    @BeforeEach
    void setUp() {
        List<String> channels = List.of("EEG Fp1", "EEG Fp2", "EEG F7", "EEG F3", "EEG Fz");
        mockMetadata = new EdfMetadata(1L, "Sample EDF", "Patient-123", 31, 300.0, 5, "2025-02-12", channels);
    }

    @Test
    void testGetEdfDescriptor_Success() throws IOException {
        when(authService.isValidKey(validKey)).thenReturn(true);
        when(edfFileService.processEdfFile(validUrl)).thenReturn(mockMetadata);

        ResponseEntity<?> response = edfController.getEdfDescriptor(validKey, validUrl);

        assertEquals(OK, response.getStatusCode());
        assertEquals(mockMetadata, response.getBody());
    }

    @Test
    void testGetEdfDescriptor_MissingKey() {
        ResponseEntity<?> response = edfController.getEdfDescriptor("", validUrl);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Authentication key is missing.", response.getBody());
    }

    @Test
    void testGetEdfDescriptor_InvalidKey() {
        when(authService.isValidKey(invalidKey)).thenReturn(false);

        ResponseEntity<?> response = edfController.getEdfDescriptor(invalidKey, validUrl);

        assertEquals(FORBIDDEN, response.getStatusCode());
        assertEquals("Invalid authentication key.", response.getBody());
    }

    @Test
    void testGetEdfDescriptor_MissingFileUrl() {
        when(authService.isValidKey(validKey)).thenReturn(true);

        ResponseEntity<?> response = edfController.getEdfDescriptor(validKey, "");

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("File URL is required.", response.getBody());
    }

    @Test
    void testGetEdfDescriptor_InvalidFileURLException() throws IOException {
        when(authService.isValidKey(validKey)).thenReturn(true);
        when(edfFileService.processEdfFile(invalidUrl)).thenThrow(new InvalidFileURLException("Invalid EDF file URL."));

        ResponseEntity<?> response = edfController.getEdfDescriptor(validKey, invalidUrl);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid EDF file URL.", response.getBody());
    }

    @Test
    void testGetEdfDescriptor_FileProcessingException() throws IOException {
        when(authService.isValidKey(validKey)).thenReturn(true);
        when(edfFileService.processEdfFile(validUrl)).thenThrow(new FileProcessingException("Error processing EDF file.", new Exception()));

        ResponseEntity<?> response = edfController.getEdfDescriptor(validKey, validUrl);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error processing EDF file.", response.getBody());
    }

    @Test
    void testGetEdfDescriptor_UnexpectedException() throws IOException {
        when(authService.isValidKey(validKey)).thenReturn(true);
        when(edfFileService.processEdfFile(validUrl)).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> response = edfController.getEdfDescriptor(validKey, validUrl);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred.", response.getBody());
    }
}

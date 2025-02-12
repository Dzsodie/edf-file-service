package com.zetoinc.edf_file_service.service;

import com.zetoinc.edf_file_service.exception.FileProcessingException;
import com.zetoinc.edf_file_service.exception.InvalidFileURLException;
import com.zetoinc.edf_file_service.model.EdfMetadata;
import com.zetoinc.edf_file_service.repository.EdfMetadataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link EdfFileServiceImpl}.
 * Ensures correct behavior for EDF file processing.
 */
@ExtendWith(MockitoExtension.class)
class EdfFileServiceImplTest {

    @Mock
    private EdfMetadataRepository repository;

    @InjectMocks
    private EdfFileServiceImpl edfFileService;

    private static final String VALID_EDF_URL = "https://example.com/sample.edf";
    private static final String INVALID_EDF_URL = "ftp://example.com/sample.edf";
    private static final String MALFORMED_URL = "htp:/invalid-url";

    @BeforeEach
    void setUp() {
        // Ensure repository mock does not have unnecessary stubs
        reset(repository);
    }

    @Test
    void testProcessEdfFile_InvalidUrl_ThrowsInvalidFileURLException() {
        // Act & Assert
        assertThrows(InvalidFileURLException.class, () -> edfFileService.processEdfFile(INVALID_EDF_URL),
                "Expected InvalidFileURLException for invalid protocol");

        verify(repository, never()).save(any(EdfMetadata.class));
    }

    @Test
    void testProcessEdfFile_MalformedUrl_ThrowsInvalidFileURLException() {
        // Act & Assert
        assertThrows(InvalidFileURLException.class, () -> edfFileService.processEdfFile(MALFORMED_URL),
                "Expected InvalidFileURLException for malformed URL");

        verify(repository, never()).save(any(EdfMetadata.class));
    }

    @Test
    void testProcessEdfFile_NullUrl_ThrowsInvalidFileURLException() {
        // Act & Assert
        assertThrows(InvalidFileURLException.class, () -> edfFileService.processEdfFile(null),
                "Expected InvalidFileURLException for null URL");

        verify(repository, never()).save(any(EdfMetadata.class));
    }

    @Test
    void testProcessEdfFile_EmptyUrl_ThrowsInvalidFileURLException() {
        // Act & Assert
        assertThrows(InvalidFileURLException.class, () -> edfFileService.processEdfFile(""),
                "Expected InvalidFileURLException for empty URL");

        verify(repository, never()).save(any(EdfMetadata.class));
    }

    @Test
    void testProcessEdfFile_WhitespaceUrl_ThrowsInvalidFileURLException() {
        // Act & Assert
        assertThrows(InvalidFileURLException.class, () -> edfFileService.processEdfFile("   "),
                "Expected InvalidFileURLException for whitespace URL");

        verify(repository, never()).save(any(EdfMetadata.class));
    }
}

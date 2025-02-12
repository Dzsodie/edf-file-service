package com.zetoinc.edf_file_service.controller;

import com.zetoinc.edf_file_service.exception.FileProcessingException;
import com.zetoinc.edf_file_service.exception.InvalidFileURLException;
import com.zetoinc.edf_file_service.model.EdfMetadata;
import com.zetoinc.edf_file_service.security.AuthService;
import com.zetoinc.edf_file_service.service.EdfFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;

/**
 * REST controller for handling EDF file metadata extraction.
 * <p>
 * This controller provides an API endpoint to process EDF files by retrieving their metadata.
 * The API requires authentication using a pre-shared key (PSK).
 * </p>
 */
@RestController
@RequestMapping("/api/edf")
@Tag(name = "EDF File Service", description = "API for extracting EDF file metadata")
public class EdfController {

    private static final Logger logger = LoggerFactory.getLogger(EdfController.class);

    private final AuthService authService;
    private final EdfFileService edfFileService;

    /**
     * Constructs an instance of {@code EdfController}.
     *
     * @param authService   The authentication service used for request validation.
     * @param edfFileService The service responsible for processing EDF files.
     */
    public EdfController(AuthService authService, EdfFileService edfFileService) {
        this.authService = authService;
        this.edfFileService = edfFileService;
    }

    /**
     * Retrieves the metadata of an EDF file from the given URL.
     * <p>
     * This endpoint requires authentication via a pre-shared key (PSK).
     * If the key is invalid or the file URL is incorrect, appropriate error responses are returned.
     * </p>
     *
     * @param key     The pre-shared key for authentication.
     * @param fileUrl The URL of the EDF file.
     * @return A response entity containing the metadata of the EDF file or an appropriate error message.
     */
    @GetMapping("/descriptor")
    @Operation(summary = "Get EDF file descriptor",
            description = "Fetches metadata of an EDF file by providing a valid authentication key and file URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved EDF metadata"),
            @ApiResponse(responseCode = "400", description = "Bad request: Missing or invalid parameters"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Invalid authentication key"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error: Unexpected error occurred")
    })
    public ResponseEntity<?> getEdfDescriptor(
            @Parameter(description = "Pre-Shared Key for authentication", required = true)
            @RequestParam String key,

            @Parameter(description = "URL pointing to the EDF file", required = true)
            @RequestParam String fileUrl) {

        logger.info("Received request to process EDF file from URL: {}", fileUrl);

        // Validate authentication key
        if (!StringUtils.hasText(key)) {
            logger.warn("Authentication failed: Missing key");
            return ResponseEntity.badRequest().body("Authentication key is missing.");
        }

        if (!authService.isValidKey(key)) {
            logger.warn("Authentication failed: Invalid key");
            return ResponseEntity.status(403).body("Invalid authentication key.");
        }

        // Validate file URL
        if (!StringUtils.hasText(fileUrl)) {
            logger.warn("Invalid request: Missing file URL");
            return ResponseEntity.badRequest().body("File URL is required.");
        }

        try {
            EdfMetadata metadata = edfFileService.processEdfFile(fileUrl);
            logger.info("Successfully processed EDF file from URL: {}", fileUrl);
            return ResponseEntity.ok(metadata);
        } catch (InvalidFileURLException e) {
            logger.error("Invalid file URL provided: {}", fileUrl, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (FileProcessingException e) {
            logger.error("Error processing EDF file: {}", fileUrl, e);
            return ResponseEntity.internalServerError().body("Error processing EDF file.");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while processing EDF file: {}", fileUrl, e);
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }
}

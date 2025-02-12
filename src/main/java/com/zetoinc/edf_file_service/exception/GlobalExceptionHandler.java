package com.zetoinc.edf_file_service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the application.
 * <p>
 * This class provides centralized exception handling for all REST controllers.
 * It catches specific exceptions and returns appropriate HTTP responses.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles {@link InvalidFileURLException} exceptions.
     * <p>
     * This exception occurs when an invalid EDF file URL is provided.
     * </p>
     *
     * @param ex The exception instance containing details of the error.
     * @return A {@link ResponseEntity} with a 400 Bad Request status and an error message.
     */
    @ExceptionHandler(InvalidFileURLException.class)
    public ResponseEntity<String> handleInvalidFileURLException(InvalidFileURLException ex) {
        logger.warn("Handled InvalidFileURLException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Handles {@link FileProcessingException} exceptions.
     * <p>
     * This exception occurs when there is an issue processing an EDF file, such as file not found or I/O errors.
     * </p>
     *
     * @param ex The exception instance containing details of the error.
     * @return A {@link ResponseEntity} with a 500 Internal Server Error status and an error message.
     */
    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<String> handleFileProcessingException(FileProcessingException ex) {
        logger.error("Handled FileProcessingException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    /**
     * Handles all uncaught exceptions in the application.
     * <p>
     * This is a fallback handler for any unexpected errors that are not specifically handled.
     * </p>
     *
     * @param ex The exception instance containing details of the error.
     * @return A {@link ResponseEntity} with a 500 Internal Server Error status and a generic error message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        logger.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }
}

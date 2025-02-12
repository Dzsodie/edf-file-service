package com.zetoinc.edf_file_service.exception;

/**
 * Exception thrown when an error occurs while processing an EDF file.
 * <p>
 * This exception is triggered in cases such as file download failures,
 * file format issues, or unexpected I/O errors.
 * </p>
 */
public class FileProcessingException extends RuntimeException {

    /**
     * Constructs a new {@code FileProcessingException} with the specified detail message and cause.
     *
     * @param message The error message describing the reason for the exception.
     * @param cause   The underlying cause of the exception (e.g., {@link java.io.IOException}).
     */
    public FileProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}

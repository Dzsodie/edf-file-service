package com.zetoinc.edf_file_service.exception;

/**
 * Exception thrown when an invalid EDF file URL is provided.
 * <p>
 * This exception is triggered when the provided URL does not conform to the expected format
 * or is otherwise considered invalid for processing.
 * </p>
 */
public class InvalidFileURLException extends RuntimeException {

    /**
     * Constructs a new {@code InvalidFileURLException} with the specified detail message.
     *
     * @param message The error message describing the reason for the exception.
     */
    public InvalidFileURLException(String message) {
        super(message);
    }
}

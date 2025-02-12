package com.zetoinc.edf_file_service.service;

import com.zetoinc.edf_file_service.model.EdfMetadata;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Service interface for processing EDF (European Data Format) files.
 * <p>
 * This interface defines the contract for downloading, processing, and extracting metadata from EDF files.
 * Implementations should handle file validation, downloading, metadata extraction, and persistence.
 * </p>
 */
@Service
public interface EdfFileService {

    /**
     * Processes an EDF file from a given URL.
     * <p>
     * This method is responsible for retrieving an EDF file from the provided URL,
     * extracting its metadata, and saving the metadata in the database.
     * </p>
     *
     * @param fileUrl The URL of the EDF file to process. Must be a valid HTTP/HTTPS URL ending with ".edf".
     * @return The extracted metadata of the EDF file.
     * @throws IOException If an error occurs while accessing or processing the file.
     */
    EdfMetadata processEdfFile(String fileUrl) throws IOException;
}

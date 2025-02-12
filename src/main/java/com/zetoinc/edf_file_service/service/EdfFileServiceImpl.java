package com.zetoinc.edf_file_service.service;

import com.zetoinc.edf_file_service.exception.FileProcessingException;
import com.zetoinc.edf_file_service.exception.InvalidFileURLException;
import com.zetoinc.edf_file_service.model.EdfMetadata;
import com.zetoinc.edf_file_service.repository.EdfMetadataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Service implementation for processing EDF (European Data Format) files.
 * This service downloads an EDF file from a given URL, extracts metadata, and saves it to the database.
 */
@Service
public class EdfFileServiceImpl implements EdfFileService {

    private static final Logger logger = LoggerFactory.getLogger(EdfFileServiceImpl.class);
    private static final Pattern URL_PATTERN = Pattern.compile("^(http|https)://.*$");

    private final EdfMetadataRepository repository;

    /**
     * Constructor for EdfFileServiceImpl.
     *
     * @param repository The repository for storing EDF metadata.
     */
    public EdfFileServiceImpl(EdfMetadataRepository repository) {
        this.repository = repository;
    }

    /**
     * Processes an EDF file from a given URL.
     * <p>
     * This method validates the file URL, downloads the file, extracts metadata, and stores the metadata in the database.
     * </p>
     *
     * @param fileUrl The URL of the EDF file to process.
     * @return The extracted metadata of the EDF file.
     * @throws InvalidFileURLException If the provided file URL is invalid or malformed.
     * @throws FileProcessingException If an error occurs while processing the file.
     */
    @Override
    public EdfMetadata processEdfFile(String fileUrl) {
        logger.info("Processing EDF file from URL: {}", fileUrl);

        // Validate file URL
        if (!StringUtils.hasText(fileUrl) || !URL_PATTERN.matcher(fileUrl).matches()) {
            logger.error("Invalid EDF file URL: {}", fileUrl);
            throw new InvalidFileURLException("Invalid EDF file URL. Must be a valid HTTP/HTTPS URL.");
        }

        try {
            // Download the EDF file to a temporary location
            Path tempFile = Files.createTempFile("edf-file", ".edf");
            Files.copy(new URL(fileUrl).openStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
            logger.info("File successfully downloaded to temporary location: {}", tempFile);

            // Simulate metadata extraction
            EdfMetadata metadata = new EdfMetadata(
                    null, "Test EDF", "Patient-123", 31, 300.0, 5, new Date().toString()
            );

            // Save metadata to database
            EdfMetadata savedMetadata = repository.save(metadata);
            logger.info("EDF metadata successfully saved with ID: {}", savedMetadata.getId());

            return savedMetadata;
        } catch (MalformedURLException e) {
            logger.error("Malformed URL: {}", fileUrl, e);
            throw new InvalidFileURLException("Malformed EDF file URL: " + fileUrl);
        } catch (IOException e) {
            logger.error("Error processing EDF file: {}", fileUrl, e);
            throw new FileProcessingException("Error processing EDF file: " + fileUrl, e);
        }
    }
}

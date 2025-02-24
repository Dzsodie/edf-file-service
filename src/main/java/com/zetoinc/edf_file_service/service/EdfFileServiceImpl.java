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
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Service implementation for processing EDF (European Data Format) files.
 * This service downloads an EDF file from a given URL, extracts metadata, and saves it to the database.
 */
@Service
public class EdfFileServiceImpl implements EdfFileService {

    private static final Logger logger = LoggerFactory.getLogger(EdfFileServiceImpl.class);
    private static final Pattern URL_PATTERN = Pattern.compile("^(http|https|file)://.*$");
    private static final int HEADER_SIZE = 256;
    private static final int LABEL_SIZE = 16;

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

            // Extract metadata
            EdfMetadata metadata = extractEdfMetadata(tempFile);

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
    private EdfMetadata extractEdfMetadata(Path filePath) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(filePath.toFile(), "r")) {
            /*// Validate the file starts with EDF format (First 8 bytes should contain "0       EDF")
            raf.seek(0);
            byte[] edfHeaderBytes = new byte[8];
            raf.readFully(edfHeaderBytes);
            String edfHeader = new String(edfHeaderBytes, StandardCharsets.UTF_8).trim();

            if (!edfHeader.contains("EDF")) {
                logger.error("Invalid EDF file: Header does not contain 'EDF'. Read: {}", edfHeader);
                throw new FileProcessingException("Invalid EDF file format.", new Throwable().getCause());
            }
*/
            // Extract Patient ID (Byte 8, 80 bytes long)
            raf.seek(168);
            byte[] patientIdBytes = new byte[20];
            raf.readFully(patientIdBytes);
            String patientId = new String(patientIdBytes, StandardCharsets.UTF_8).trim();

            // Extract Start Date (Byte 88, 8 bytes long)
            raf.seek(97);
            byte[] startDateBytes = new byte[16];
            raf.readFully(startDateBytes);
            String startDate = new String(startDateBytes, StandardCharsets.UTF_8).trim();

            // Extract Duration (Byte 244, 8 bytes long)
            raf.seek(244);
            byte[] durationBytes = new byte[8];
            raf.readFully(durationBytes);
            String durationStr = new String(durationBytes, StandardCharsets.UTF_8).trim();
            double duration = parseDoubleSafely(durationStr, 0.0);

            // Extract Number of Annotations (Byte 236, 4 bytes long)
            raf.seek(236);
            byte[] numAnnotationsBytes = new byte[4];
            raf.readFully(numAnnotationsBytes);
            String numAnnotationsStr = new String(numAnnotationsBytes, StandardCharsets.UTF_8).trim();
            int numAnnotations = parseIntSafely(numAnnotationsStr, 0);

            // Extract Number of Channels (Byte 252, 4 bytes long)
            raf.seek(252);
            byte[] numChannelsBytes = new byte[4];
            raf.readFully(numChannelsBytes);
            int numChannels = parseIntSafely(new String(numChannelsBytes).trim(), 0);

            // Extract Channel Names (Start from Byte 256, each label is 16 bytes)
            List<String> channelNames = new ArrayList<>();
            raf.seek(HEADER_SIZE);
            for (int i = 0; i < numChannels; i++) {
                byte[] labelBytes = new byte[LABEL_SIZE];
                raf.readFully(labelBytes);
                channelNames.add(new String(labelBytes, StandardCharsets.UTF_8).trim());
            }

            return new EdfMetadata(null, "EDF File", patientId, numChannels, duration, numAnnotations, startDate, channelNames);
        }
    }

    /**
     * Safely parses a string into a double value. Returns a default value if parsing fails.
     */
    private double parseDoubleSafely(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Safely parses a string into an integer value. Returns a default value if parsing fails.
     */
    private int parseIntSafely(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}

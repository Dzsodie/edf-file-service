package com.zetoinc.edf_file_service.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Implementation of {@link AuthService} that provides authentication
 * using a pre-shared key (PSK) mechanism.
 * <p>
 * This service validates access by comparing the provided key with a predefined secret key.
 * </p>
 */
@Component
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    @Value("${app.secret.key}")
    private String secretKey;

    /**
     * Validates the provided authentication key.
     * <p>
     * This method checks if the provided key matches the predefined secret key.
     * </p>
     *
     * @param key The authentication key provided by the client.
     * @return {@code true} if the key is valid, {@code false} otherwise.
     */
    @Override
    public boolean isValidKey(String key) {
        // Null and empty check for key
        if (!StringUtils.hasText(key)) {
            logger.warn("Authentication failed: Provided key is null or empty.");
            return false;
        }

        // Compare provided key with the predefined secret key
        boolean isValid = secretKey.equals(key);
        if (isValid) {
            logger.info("Authentication successful.");
        } else {
            logger.warn("Authentication failed: Invalid key provided.");
        }
        return isValid;
    }
}

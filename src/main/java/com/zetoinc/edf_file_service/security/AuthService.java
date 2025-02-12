package com.zetoinc.edf_file_service.security;

/**
 * Service interface for authentication.
 * <p>
 * This interface defines the contract for validating authentication keys.
 * Implementations should provide logic to verify the provided key against a
 * pre-configured or dynamically managed authentication mechanism.
 * </p>
 */
public interface AuthService {

    /**
     * Validates the provided authentication key.
     * <p>
     * This method checks if the given key is valid and grants or denies access accordingly.
     * </p>
     *
     * @param key The authentication key provided by the client.
     * @return {@code true} if the key is valid, {@code false} otherwise.
     */
    boolean isValidKey(String key);
}

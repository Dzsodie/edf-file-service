package com.zetoinc.edf_file_service.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.StringUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for {@link AuthServiceImpl} using JUnit 5 and Mockito.
 * This test verifies the authentication mechanism with various key inputs.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    private static final String VALID_SECRET_KEY = "very_secret_key";

    @BeforeEach
    void setUp() {
        // Inject the secret key into the AuthServiceImpl instance
        ReflectionTestUtils.setField(authService, "secretKey", VALID_SECRET_KEY);
    }

    @Test
    void testIsValidKey_ValidKey() {
        // Given a valid key
        String validKey = VALID_SECRET_KEY;

        // When calling the method
        boolean result = authService.isValidKey(validKey);

        // Then the result should be true
        assertTrue(result, "Expected valid key to return true");
    }

    @Test
    void testIsValidKey_InvalidKey() {
        // Given an invalid key
        String invalidKey = "wrong_key";

        // When calling the method
        boolean result = authService.isValidKey(invalidKey);

        // Then the result should be false
        assertFalse(result, "Expected invalid key to return false");
    }

    @Test
    void testIsValidKey_NullKey() {
        // Given a null key
        String nullKey = null;

        // When calling the method
        boolean result = authService.isValidKey(nullKey);

        // Then the result should be false
        assertFalse(result, "Expected null key to return false");
    }

    @Test
    void testIsValidKey_EmptyKey() {
        // Given an empty key
        String emptyKey = "";

        // When calling the method
        boolean result = authService.isValidKey(emptyKey);

        // Then the result should be false
        assertFalse(result, "Expected empty key to return false");
    }

    @Test
    void testIsValidKey_WhitespaceKey() {
        // Given a key with only whitespace
        String whitespaceKey = "   ";

        // When calling the method
        boolean result = authService.isValidKey(whitespaceKey);

        // Then the result should be false
        assertFalse(result, "Expected whitespace key to return false");
    }

    @Test
    void testIsValidKey_StringUtilsMocked() {
        // Mock StringUtils to always return false for key validation
        try (MockedStatic<StringUtils> mockedStringUtils = mockStatic(StringUtils.class)) {
            mockedStringUtils.when(() -> StringUtils.hasText(anyString())).thenReturn(false);

            boolean result = authService.isValidKey("some_key");

            assertFalse(result, "Expected mocked StringUtils to return false");
        }
    }
}

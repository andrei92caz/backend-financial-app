package com.financial.auth;

import com.financial.auth.controller.AuthController;
import com.financial.auth.dto.RegisterRequest;
import com.financial.auth.service.AuthService;
import com.google.firebase.auth.FirebaseAuthException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterSuccess() throws FirebaseAuthException {
        // Arrange
        RegisterRequest request = new RegisterRequest("test@example.com", "password123");
        when(authService.register(request)).thenReturn("12345");

        // Act
        ResponseEntity<?> response = authController.register(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully with ID: 12345", response.getBody());
    }

    @Test
    void testRegisterFailure() throws FirebaseAuthException {
        // Arrange
        RegisterRequest request = new RegisterRequest("test@example.com", "password123");
        FirebaseAuthException mockException = mock(FirebaseAuthException.class);
        when(mockException.getMessage()).thenReturn("Registration failed");
        when(authService.register(request)).thenThrow(mockException);


        // Act
        ResponseEntity<?> response = authController.register(request);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error registering user: Registration failed", response.getBody());
    }

    @Test
    void testLoginSuccess() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest("test@example.com", "password123");
        when(authService.loginWithEmailAndPassword(request)).thenReturn("mock-token");

        // Act
        ResponseEntity<?> response = authController.login(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Token: mock-token", response.getBody());
    }

    @Test
    void testLoginFailure() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest("test@example.com", "password123");
        when(authService.loginWithEmailAndPassword(request)).thenThrow(new RuntimeException("Invalid credentials"));

        // Act
        ResponseEntity<?> response = authController.login(request);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Login failed: Invalid credentials", response.getBody());
    }
}

package com.financial.auth;

import com.financial.auth.dto.RegisterRequest;
import com.financial.auth.service.AuthService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {


    @Mock
    private FirebaseAuth firebaseAuth;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private UserRecord mockUserRecord;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        firebaseAuth = mock(FirebaseAuth.class);
        authService = new AuthService(firebaseAuth, restTemplate);
    }

    @Test
    void testRegisterSuccess() throws FirebaseAuthException {
        RegisterRequest request = new RegisterRequest("test@example.com", "password123");

        when(firebaseAuth.createUser(any())).thenReturn(mockUserRecord);
        when(mockUserRecord.getUid()).thenReturn("12345");

        String userId = authService.register(request);

        assertEquals("12345", userId);
        verify(firebaseAuth).createUser(any());
    }

    @Test
    void testRegisterFailure() throws FirebaseAuthException {
        // Arrange
        RegisterRequest request = new RegisterRequest("test@example.com", "password123");
        FirebaseAuthException mockException = mock(FirebaseAuthException.class);
        when(mockException.getMessage()).thenReturn("Registration failed");
        when(firebaseAuth.createUser(any(UserRecord.CreateRequest.class))).thenThrow(mockException);

        // Act & Assert
        FirebaseAuthException exception = assertThrows(FirebaseAuthException.class, () -> authService.register(request));
        assertEquals("Registration failed", exception.getMessage());
    }

    @Test
    void loginWithEmailAndPassword_success() throws Exception {
        RegisterRequest request = new RegisterRequest("test@email.com", "test123");

        String jsonResponse = "{ \"idToken\": \"fake-token-123\" }";

        ResponseEntity<String> response = new ResponseEntity<>(jsonResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(response);

        String token = authService.loginWithEmailAndPassword(request);

        assertEquals("fake-token-123", token);
    }

    @Test
    void loginWithEmailAndPassword_failure() {
        RegisterRequest request = new RegisterRequest("wrong@email.com", "badpass");

        ResponseEntity<String> response = new ResponseEntity<>("Bad credentials", HttpStatus.UNAUTHORIZED);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(response);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.loginWithEmailAndPassword(request);
        });

        assertTrue(ex.getMessage().contains("Authentication failed"));
    }

    @Test
    void sendPasswordResetEmail_success() {
        ResponseEntity<String> response = new ResponseEntity<>("", HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(response);

        assertDoesNotThrow(() -> authService.sendPasswordResetEmail("test@example.com"));
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void sendPasswordResetEmail_firebaseError_doesNotThrow() {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenThrow(new RuntimeException("Firebase error"));

        assertDoesNotThrow(() -> authService.sendPasswordResetEmail("test@example.com"));
    }
}


package com.financial.auth.controller;

import com.financial.auth.dto.ForgotPasswordRequest;
import com.financial.auth.dto.RegisterRequest;
import com.financial.auth.dto.AuthResponse;
import com.financial.auth.service.AuthService;
import com.google.firebase.auth.FirebaseAuthException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            String userId = authService.register(request);
            AuthResponse response = new AuthResponse(
                "User registered successfully with ID: " + userId,
                null,
                userId
            );
            return ResponseEntity.ok(response);
        } catch (FirebaseAuthException e) {
            AuthResponse errorResponse = new AuthResponse(
                "Error registering user: " + e.getMessage(),
                null,
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody RegisterRequest request) {
        try {
            String token = authService.loginWithEmailAndPassword(request);
            AuthResponse response = new AuthResponse(
                "Login successful",
                token,
                null
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            AuthResponse errorResponse = new AuthResponse(
                "Login failed: " + e.getMessage(),
                null,
                null
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request, HttpServletRequest httpRequest) {
        String ip = httpRequest.getRemoteAddr();
        log.info("Password reset requested for email: {} from IP: {}", request.getEmail(), ip);
        authService.sendPasswordResetEmail(request.getEmail());
        return ResponseEntity.ok("If an account with that email exists, a password reset link has been sent.");
    }
}
package com.financial.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financial.auth.dto.RegisterRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {
    @Value("${firebase.api.key}")
    private String FIREBASE_API_KEY;

    private final FirebaseAuth firebaseAuth;
    private final RestTemplate restTemplate;

    public AuthService(FirebaseAuth firebaseAuth, RestTemplate restTemplate) {
        this.firebaseAuth = firebaseAuth;
        this.restTemplate = restTemplate;
    }

    public String register(RegisterRequest request) throws FirebaseAuthException {
        UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest()
                .setEmail(request.getEmail())
                .setPassword(request.getPassword());

        UserRecord userRecord = firebaseAuth.createUser(createRequest);
        return userRecord.getUid();
    }

    public FirebaseToken verifyToken(String idToken) throws FirebaseAuthException {
        return firebaseAuth.verifyIdToken(idToken);
    }

    public String loginWithEmailAndPassword(RegisterRequest request) throws Exception{
        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + FIREBASE_API_KEY;

        String payload = String.format(
                "{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}",
                request.getEmail(), request.getPassword()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK){
            ObjectMapper mapper = new ObjectMapper();
            JsonNode body = mapper.readTree(response.getBody());
            return body.get("idToken").asText();
        } else {
            throw new RuntimeException("Authentication failed: " + response.getBody());
        }


    }
}

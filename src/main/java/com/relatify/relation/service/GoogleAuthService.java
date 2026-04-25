package com.relatify.relation.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.relatify.relation.model.User;
import com.relatify.relation.repository.UserRepository;
import com.relatify.relation.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleAuthService {

    private final UserRepository userRepository;

    // IMPORTANT: replace with your Google Client ID
    private static final String CLIENT_ID = "958380841417-41i2hv91a57oirgr30k0p9b655alhjme.apps.googleusercontent.com";

    public GoogleAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, Object> authenticate(String idTokenString) throws Exception {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new GsonFactory()
        )
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);

        if (idToken == null) {
            throw new RuntimeException("Invalid Google token");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();

        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String googleId = payload.getSubject();
        String picture = (String) payload.get("picture");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User u = new User();
                    u.setEmail(email);
                    u.setName(name);
                    u.setGoogleId(googleId);
                    u.setPicture(picture);
                    return userRepository.save(u);
                });

        String jwt = JwtUtil.generateToken(email);

        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("user", user);

        return response;
    }
}
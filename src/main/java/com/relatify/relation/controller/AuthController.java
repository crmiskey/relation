package com.relatify.relation.controller;

import com.relatify.relation.service.GoogleAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // React dev server
public class AuthController {

    private final GoogleAuthService googleAuthService;

    public AuthController(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> request) throws Exception {

        String token = request.get("token");
        System.out.println("🔥 HIT GOOGLE LOGIN API");
        return ResponseEntity.ok("test");

/*        return ResponseEntity.ok(
                googleAuthService.authenticate(token)
        );*/
    }
}

package com.example.uon.controller;

import com.example.uon.model.User;
import com.example.uon.model.UserRole;
import com.example.uon.service.UserService;
import com.google.common.net.MediaType;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<byte[]> getAuthPage() throws IOException {
        ClassPathResource htmlFile = new ClassPathResource("static/auth/index.html");
        byte[] bytes = Files.readAllBytes(htmlFile.getFile().toPath());
        return ResponseEntity.ok().body(bytes);
    }
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestHeader("Authorization") String idToken, @RequestBody RoleRequest roleRequest) {
        System.out.println("Received ID Token: " + idToken);
        try {
            // The token from the client should be "Bearer <token>"
            String token = idToken.substring(7);
            User newUser = userService.registerUser(token, roleRequest.getRole());
            return ResponseEntity.ok(newUser);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(401).build();
        } catch (IllegalStateException e) {
            System.out.println("User already registered: ");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // 409 Conflict
        }
    }

    // A simple DTO for the role request
    static class RoleRequest {
        private UserRole role;
        public UserRole getRole() { return role; }
        public void setRole(UserRole role) { this.role = role; }
    }
}

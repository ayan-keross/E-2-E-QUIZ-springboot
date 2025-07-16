package com.example.uon.controller;

import com.example.uon.model.User;
import com.example.uon.model.UserRole;
import com.example.uon.service.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.URI;
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
    public ResponseEntity<?> register(@RequestHeader("Authorization") String idToken, @RequestBody RoleRequest roleRequest) {
        System.out.println("Received ID Token: " + idToken);
        try {
            // The token from the client should be "Bearer <token>"
            String token = idToken.substring(7);
            User newUser = userService.registerUser(token, roleRequest.getRole());
            if (roleRequest.getRole().toString().equals("TUTOR")) {
                System.out.println("User is a tutor, redirecting to tutor home. for new user");
                URI redirectUri = URI.create("/tutor/");
                return ResponseEntity.status(HttpStatus.SEE_OTHER)
                                     .location(redirectUri)
                                     .build();
            }
            return ResponseEntity.ok(newUser);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(401).build();
        } catch (IllegalStateException e) {
            System.out.println("User already registered: from catch");
            try {
                User userRole = userService.getUserByFirebaseUid(idToken.substring(7));
                System.out.println("User role: " + userRole);
                if (userRole.getRole().toString().equals("TUTOR")) {
                    System.out.println("User is a tutor, redirecting to tutor home.");
                    URI redirectUri = URI.create("http://localhost:8080/api/tutor/");
                    return ResponseEntity.status(HttpStatus.SEE_OTHER)
                                         .location(redirectUri)
                                         .build();
                } else if (userRole.getRole().equals(UserRole.STUDENT)) {
                    System.out.println("User is a student, redirecting to student home.");
                    URI redirectUri = URI.create("http://localhost:8080/api/student/");
                    System.out.println("Redirect URI: " + redirectUri);
                    return ResponseEntity.status(HttpStatus.SEE_OTHER)
                                         .location(redirectUri)
                                         .build();
                }
            } catch (FirebaseAuthException ex) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
           
        }
        // If none of the above return statements are executed, return a generic error response
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
    }

    // A simple DTO for the role request
    static class RoleRequest {
        private UserRole role;
        public UserRole getRole() { return role; }
        public void setRole(UserRole role) { this.role = role; }
    }
}

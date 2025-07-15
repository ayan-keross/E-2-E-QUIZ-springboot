package com.example.uon.controller;

import com.example.uon.model.User;
import com.example.uon.model.UserRole;
import com.example.uon.service.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestHeader("Authorization") String idToken, @RequestBody RoleRequest roleRequest) {
        try {
            // The token from the client should be "Bearer <token>"
            String token = idToken.substring(7);
            User newUser = userService.registerUser(token, roleRequest.getRole());
            return ResponseEntity.ok(newUser);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(401).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(null); // 409 Conflict
        }
    }

    // A simple DTO for the role request
    static class RoleRequest {
        private UserRole role;
        public UserRole getRole() { return role; }
        public void setRole(UserRole role) { this.role = role; }
    }
}

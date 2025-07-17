package com.example.uon.service;

import com.example.uon.model.User;
import com.example.uon.model.UserRole;
import com.example.uon.dao.UserDao;
import java.util.Optional;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import jakarta.persistence.EnumType;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    

    public User registerUser(String idToken, UserRole role) throws FirebaseAuthException {
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        String uid = decodedToken.getUid();
        String email = decodedToken.getEmail();

        System.out.println("Decoded token :: "+uid);

        if (userDao.findByFirebaseUid(uid).isPresent()) {
            //throw new IOException("Test");
            throw new IllegalStateException("User already registered.");
         }

        User newUser = new User();
        newUser.setFirebaseUid(uid);
        newUser.setEmail(email);
        newUser.setRole(role);

        return userDao.save(newUser);
    }

    public User getUserByFirebaseUid(String idToken) throws FirebaseAuthException {
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        String firebaseUid = decodedToken.getUid();
        return userDao.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }


    // public Optional<User> getUserRoleByFirebaseUid(String firebaseUid) {
    //     Optional<User> userRole = userDao.findByFirebaseUid(firebaseUid);
    //     System.out.println("User role from service: " + userRole);
    //     return userRole;
    // }

    public String getRoleByFirebaseUid(String firebaseUid) {
        // 🔍 Fetch role from MySQL DB
        // Assuming userDao has a method to get role by Firebase UID
        
        User user = userDao.getRoleByFirebaseUid(firebaseUid);
        if (user == null) {
            throw new RuntimeException("Role not found for Firebase UID: " + firebaseUid);
        }else{
            System.out.println("User role from service: " + user.getRole());
            return user.getRole().toString(); // Returns "TUTOR" or "STUDENT"
        }
    }


}
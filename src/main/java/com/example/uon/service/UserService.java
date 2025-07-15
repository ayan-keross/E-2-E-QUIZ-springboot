package com.example.uon.service;

import com.example.uon.model.User;
import com.example.uon.model.UserRole;
import com.example.uon.dao.UserDao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    

    public User registerUser(String idToken, UserRole role) throws FirebaseAuthException {
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        String uid = decodedToken.getUid();
        String email = decodedToken.getEmail();

        if (userDao.findByFirebaseUid(uid).isPresent()) {
            throw new IllegalStateException("User already registered.");
        }

        User newUser = new User();
        newUser.setFirebaseUid(uid);
        newUser.setEmail(email);
        newUser.setRole(role);

        return userDao.save(newUser);
    }
}
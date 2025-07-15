package com.example.uon.dao;

import com.example.uon.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {
    Optional<User> findByFirebaseUid(String firebaseUid);
}
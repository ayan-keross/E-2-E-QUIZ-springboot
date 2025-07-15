package com.example.uon.security;

import com.example.uon.model.User;
import com.example.uon.dao.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDao userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Here, "username" is the Firebase UID
        User user = userRepository.findByFirebaseUid(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with firebase UID: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getFirebaseUid(),
                "", // Password is not needed as we use token-based auth
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}
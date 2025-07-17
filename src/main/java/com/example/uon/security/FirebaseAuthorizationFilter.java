package com.example.uon.security;

import com.example.uon.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FirebaseAuthorizationFilter extends OncePerRequestFilter {

    private final UserService userService;

    // public FirebaseAuthorizationFilter(UserService userService) {
    // this.userService = userService;
    // }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
                String uid = decodedToken.getUid();
                System.out.println("Decoded Firebase UID: from authorization filter " + uid);
                // 🔍 Get role from your MySQL DB
                String role = userService.getRoleByFirebaseUid(uid); // returns "TUTOR" or "STUDENT"

                // ✅ Inject role into Spring Security context
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
                System.out.println("Authorities: " + authorities);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(uid, null,
                        authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);
                request.setAttribute("uid", uid); // Optional: to access UID in controllers
                System.out.println("==> Injected Authorities:");
                authorities.forEach(a -> System.out.println(" - " + a.getAuthority()));

            } catch (FirebaseAuthException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Firebase token");
                return;
            } catch (RuntimeException e) {
                System.out.println("Error retrieving user role: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Role not found");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}

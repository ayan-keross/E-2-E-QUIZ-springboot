package com.example.uon.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.uon.service.StudentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @GetMapping("/get")
    public ResponseEntity<String> getStudentHome() throws IOException {
        System.out.println("Accessing student home endpoint");

        Authentication auth1 = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("==> Security Filter Chain Configuration");
        System.out.println(auth1);
        if (auth1 != null) {
            System.out.println("Authenticated User: " + auth1.getName()); // Usually UID or username
            System.out.println("Authorities: " + auth1.getAuthorities());
            System.out.println("Principal: " + auth1.getPrincipal());
            System.out.println("Is Authenticated: " + auth1.isAuthenticated());
            
        } else {
            System.out.println("No authentication found in context.");
        }

        // ClassPathResource htmlFile = new
        // ClassPathResource("static/auth/student_dashboard.html");
        // byte[] bytes = Files.readAllBytes(htmlFile.getFile().toPath());
        // return ResponseEntity.ok().body(bytes);
        // return studentService.getStudentHome();

        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("static/auth/student_dashboard.html");


        System.out.println("InputStream: ");

        if (inputStream == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("<h1>HTML file not found</h1>");
        }

        String htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/html")
                .body(htmlContent);
        //return new ResponseEntity<>("Data", HttpStatus.OK);
    }
}

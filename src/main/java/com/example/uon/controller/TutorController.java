package com.example.uon.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.uon.service.TutorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/tutor")
@RequiredArgsConstructor
public class TutorController {
    private final TutorService tutorService;
    
    
    @GetMapping("/")
    public ResponseEntity<String> getTutorHome() throws IOException {
        System.out.println("Accessing tutor home endpoint");
        // ClassPathResource htmlFile = new
        // ClassPathResource("static/auth/student_dashboard.html");
        // byte[] bytes = Files.readAllBytes(htmlFile.getFile().toPath());
        // return ResponseEntity.ok().body(bytes);
        // return studentService.getStudentHome();


        
        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("static/auth/tutor_dashboard.html");

        if (inputStream == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("<h1>HTML file not found</h1>");
        }

        String htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/html")
                .body(htmlContent);

    }
}

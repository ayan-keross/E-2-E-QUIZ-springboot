package com.example.uon.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.uon.service.TutorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tutor/")
@RequiredArgsConstructor
public class TutorController {
    private final TutorService tutorService;
    
    
    @GetMapping("/")
    public String getTutorHome() {
        return tutorService.getTutorHome();
    }
}

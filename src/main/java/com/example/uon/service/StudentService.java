package com.example.uon.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

    public String getStudentHome() {
        // TODO Auto-generated method stub
        return "Welcome to the Student Home Page!";
    }

}

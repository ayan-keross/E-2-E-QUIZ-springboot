package com.example.uon.utility;

import java.util.List;

public class ApiResponse<T> {
    private int status;
    private List<T> data;
    private String message;

    public ApiResponse(int status, List<T> data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public int getStatus() { return status; }
    public List<T> getData() { return data; }
    public String getMessage() { return message; }
}

package com.example.Project1.dto;

public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest() {}

    // Email
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    // Password
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}

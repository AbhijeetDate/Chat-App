package com.example.Project1.dto;

public class ResetPasswordRequest {
    private String email;
    private String newPassword;
    private String confirmPassword;

    // Email
    public String getEmail() {
        return this.email;
    }

    // New password
    public String getNewPassword() {
        return this.newPassword;
    }

    // Confirm password
    public String getConfirmPassword() {
        return this.confirmPassword;
    }
}

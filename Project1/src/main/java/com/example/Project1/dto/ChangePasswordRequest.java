package com.example.Project1.dto;

public class ChangePasswordRequest {
    private String email;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    // Email
    public String getEmail() {
        return email;
    }

    // Old password
    public String getOldPassword() {
        return oldPassword;
    }

    // New password
    public String getNewPassword() {
        return newPassword;
    }

    // Confirm password
    public String getConfirmPassword() {
        return confirmPassword;
    }
}

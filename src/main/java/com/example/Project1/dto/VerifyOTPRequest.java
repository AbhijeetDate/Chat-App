package com.example.Project1.dto;

public class VerifyOTPRequest {
    private String email;
    private String otp;

    // Email
    public String getEmail() {
        return this.email;
    }

    // OTP
    public String getOtp() {
        return this.otp;
    }
}

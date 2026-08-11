package com.example.Project1.dto;

public class LoginResponse {

    private String message;
    private Integer userId;
    private String fullName;
    private String email;
    private String profilePictureUrl;

    // Default constructor
    public LoginResponse() {
    }

    // Parameterized constructor
    public LoginResponse(
            String message,
            Integer userId,
            String fullName,
            String email,
            String profilePictureUrl) {

        this.message = message;
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
    }

    // Getters
    public String getMessage() {
        return message;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    // Setters
    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
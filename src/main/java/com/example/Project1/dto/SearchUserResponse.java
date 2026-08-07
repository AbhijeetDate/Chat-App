package com.example.Project1.dto;

public class SearchUserResponse {

    private String email;
    private String phone;
    private String fullName;

    public SearchUserResponse() {
    }

    public SearchUserResponse(String email, String phone, String fullName) {
        this.email = email;
        this.phone = phone;
        this.fullName = fullName;
    }

    //Getters
    public String getEmail() {
        return this.email;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getFullName() {
        return this.fullName;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
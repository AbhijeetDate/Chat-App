package com.example.Project1.dto;

public class ConnectedUsersResponse {

    private Integer id;
    private String fullName;
    private String email;
    private String phone;
    private String profilePictureUrl;

    // Default constructor
    public ConnectedUsersResponse() {
    }

    // Parameterized constructor
    public ConnectedUsersResponse(
            Integer id,
            String fullName,
            String email,
            String phone,
            String profilePictureUrl) {

        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.profilePictureUrl = profilePictureUrl;
    }

    // Getters

    public Integer getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    // Setters

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
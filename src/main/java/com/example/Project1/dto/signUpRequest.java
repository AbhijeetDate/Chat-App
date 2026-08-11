package com.example.Project1.dto;

public class signUpRequest{
    private String fullName;
    private String email;
    private String phone;
    private String password;


    public signUpRequest() {}


    // Full name
    public String getFullName()
    {
        return this.fullName;
    }
    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    // Email
    public String getEmail()
    {
        return this.email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    // Phone
    public String getPhone()
    {
        return this.phone;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    // Password
    public String getPassword()
    {
        return this.password;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }
}
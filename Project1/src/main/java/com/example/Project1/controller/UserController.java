package com.example.Project1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project1.dto.ChangePasswordRequest;
import com.example.Project1.dto.ForgotPasswordRequest;
import com.example.Project1.dto.LoginRequest;
import com.example.Project1.dto.LoginResponse;
import com.example.Project1.dto.ResetPasswordRequest;
import com.example.Project1.dto.SearchUserRequest;
import com.example.Project1.dto.SearchUserResponse;
import com.example.Project1.dto.VerifyOTPRequest;
import com.example.Project1.dto.signUpRequest;
import com.example.Project1.service.UserService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public String signup(@RequestBody signUpRequest request)
    {
        return userService.signup(request);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request)
    {
        return ResponseEntity.ok(userService.login(request));
    }

    @PutMapping("/change-password")
    public String changePassword(@RequestBody ChangePasswordRequest request)
    {
        return userService.changePassword(request);
    }

    @PostMapping("/send-otp")
    public String sendOTP(@RequestBody ForgotPasswordRequest request)
    {
        return userService.generateOTP(request);
    }

    @PostMapping("/verify-otp")
    public String verifyOTP(@RequestBody VerifyOTPRequest request)
    {
        return userService.verifyOTP(request);
    }

    @PutMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest request)
    {
        return userService.resetPassword(request);
    }

    @PostMapping("/search-user")
    public ResponseEntity<?> searchUser(@RequestBody SearchUserRequest request)
    {
        SearchUserResponse response = userService.searchUser(request);
        if(response == null)
        {
            return ResponseEntity.badRequest().body("User not found.");
        }
        return ResponseEntity.ok(response);
    }
}
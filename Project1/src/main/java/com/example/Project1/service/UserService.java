package com.example.Project1.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.Project1.dto.ChangePasswordRequest;
import com.example.Project1.dto.ForgotPasswordRequest;
import com.example.Project1.dto.LoginRequest;
import com.example.Project1.dto.LoginResponse;
import com.example.Project1.dto.ResetPasswordRequest;
import com.example.Project1.dto.SearchUserRequest;
import com.example.Project1.dto.SearchUserResponse;
import com.example.Project1.dto.VerifyOTPRequest;
import com.example.Project1.dto.signUpRequest;
import com.example.Project1.entity.User;
import com.example.Project1.repository.ConnectionRepository;
import com.example.Project1.repository.UserRepository;
import com.example.Project1.util.PasswordUtil;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ConnectionRepository connectionRepository;
    @Autowired
    private PasswordUtil passwordUtil;

    @Value("${app.default-profile-url}")
    private String defaultProfileUrl;

    private Map<String, String> otpStorage = new HashMap<>();
    private Map<String, Boolean> verifiedUser = new HashMap<>();

    public String signup(signUpRequest request)
    {
        // Check if email exists
        Optional<User> emailUser = userRepository.findByEmail(request.getEmail());
        if(emailUser.isPresent())
        {
            return "Email already exists.";
        }

        // Check if phone number already exists.
        Optional<User> phoneUser = userRepository.findByPhone(request.getPhone());
        if(phoneUser.isPresent())
        {
            return "Phone number already exists.";
        }

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordUtil.encode(request.getPassword()));
        user.setProfilePictureUrl(defaultProfileUrl);

        // Save in database
        userRepository.save(user);

        return "Signup successful.";
    }

    public LoginResponse login(LoginRequest request)
    {
        Optional<User> optionalUser =
            userRepository.findByEmail(request.getEmail());

        if(optionalUser.isEmpty())
        {
            return new LoginResponse(
                "Invalid credentials.",
                null,
                null,
                null,
                null
            );
        }

        User user = optionalUser.get();

        if(!passwordUtil.matches(request.getPassword(), user.getPassword()))
        {
            return new LoginResponse(
                "Invalid credentials.",
                null,
                null,
                null,
                null
            );
        }

        return new LoginResponse(
            "Login successful.",
            user.getId(),
            user.getFullName(),
            user.getEmail(),
            user.getProfilePictureUrl()
        );
    }

    public String changePassword(ChangePasswordRequest request)
    {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if(optionalUser.isEmpty())
        {
            return "Email not found.";
        }

        User user = optionalUser.get();

        if(!passwordUtil.matches(request.getOldPassword(), user.getPassword()))
        {
            return "Old password is incorrect.";
        }
        
        if(passwordUtil.matches(request.getNewPassword(), user.getPassword()))
        {
            return "New password cannot be the same as the old password.";
        }

        if(!request.getNewPassword().equals(request.getConfirmPassword()))
        {
            return "New password and confirm password do not match.";
        }

        user.setPassword(passwordUtil.encode(request.getNewPassword()));
        userRepository.save(user);
        
        return "Password changed successfully.";
    }    

    public String generateOTP(ForgotPasswordRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if(optionalUser.isEmpty())
        {
            return "Email not found.";
        }

        User user = optionalUser.get();
        if(!user.getPhone().equals(request.getPhone()))
        {
            return "Phone number does not match.";
        }

        // Generate OTP logic here (e.g., send OTP to user's phone or email)
        Random random = new Random();
        String otp = String.valueOf(100000 + random.nextInt(900000));

        otpStorage.put(request.getEmail(), otp);

        
        emailService.sendMail(request.getEmail(), otp);

        return "OTP generated successfully.";
    }
    
    public String verifyOTP(VerifyOTPRequest request) {
        String storedOtp = otpStorage.get(request.getEmail());

        if(storedOtp == null)
        {
            return "OTP not generated.";
        }

        if(!storedOtp.equals(request.getOtp()))
        {
            return "Invalid OTP.";
        }

        verifiedUser.put(request.getEmail(), true);

        return "OTP verified successfully."; 
    }

    public String resetPassword(ResetPasswordRequest request){

        Boolean isVerified = verifiedUser.get(request.getEmail());

        if(isVerified == null || !isVerified)
        {
            return "Verify email first.";
        }

        if(!request.getNewPassword().equals(request.getConfirmPassword()))
        {
            return "New password and confirm password do not match.";
        }

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if(optionalUser.isEmpty())
        {
            return "Email not found.";
        }

        User user = optionalUser.get();

        user.setPassword(passwordUtil.encode(request.getNewPassword()));
        userRepository.save(user);

        otpStorage.remove(request.getEmail());
        verifiedUser.remove(request.getEmail());

        return "Password reset successfully.";        
    }

    public SearchUserResponse searchUser(SearchUserRequest request)
    {
        String keyword = request.getKeyword();
        Integer currentUserId = request.getCurrentUserId();

        Optional<User> user;

        if(keyword.contains("@"))
        {
            user = userRepository.findByEmail(keyword);
        }
        else
        {
            user = userRepository.findByPhone(keyword);
        }

        if(user.isEmpty())
        {
            return null;
        }

        User foundUser = user.get();
        boolean connectedStatus =
            connectionRepository
                    .findByUserId1AndUserId2(
                            currentUserId,
                            foundUser.getId()
                    )
                    .isPresent()
            ||
            connectionRepository
                    .findByUserId2AndUserId1(
                            currentUserId,
                            foundUser.getId()
                    )
                    .isPresent();

        return new SearchUserResponse(
            foundUser.getId(),
            foundUser.getFullName(),
            foundUser.getEmail(),
            foundUser.getPhone(),
            foundUser.getProfilePictureUrl(),
            connectedStatus
        );
    }
}
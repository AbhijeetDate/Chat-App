package com.example.Project1.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.Project1.dto.RemoveProfilePictureRequest;
import com.example.Project1.entity.User;
import com.example.Project1.repository.UserRepository;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Value("${app.default-profile-url}")
    private String defaultProfileUrl;

    public String updateProfilePicture(Integer userId, MultipartFile image)
    {

        Optional<User> optional = userRepository.findById(userId);

        if(optional.isEmpty())
        {
            return "User not found.";
        }

        User user = optional.get();

        String imageUrl = cloudinaryService.uploadImage(image);

        user.setProfilePictureUrl(imageUrl);

        userRepository.save(user);

        return imageUrl;
    }

    public String removeProfilePicture(RemoveProfilePictureRequest request)
    {
        Optional<User> optionalUser = userRepository.findById(request.getUserId());

        if(optionalUser.isEmpty())
        {
            return "User not found.";
        }

        User user = optionalUser.get();

        user.setProfilePictureUrl(defaultProfileUrl);

        userRepository.save(user);

        return "Profile picture removed successfully.";
    }

}
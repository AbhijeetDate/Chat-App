package com.example.Project1.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.Project1.dto.RemoveProfilePictureRequest;
import com.example.Project1.service.ProfileService;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PutMapping("/picture")
    public ResponseEntity<?> updatePicture(@RequestParam Integer userId, @RequestParam MultipartFile image) {

        String url = profileService.updateProfilePicture(userId, image);

        if (url.equals("User not found.")) 
        {
            return ResponseEntity.badRequest().body(url);
        }

        Map<String, Object> response = new HashMap<>();

        response.put("message", "Profile picture updated successfully.");
        response.put("imageUrl", url);

        return ResponseEntity.ok(response);
    }

//     @PutMapping("/picture")
// public ResponseEntity<?> updatePicture(
// @RequestParam("userId") Integer userId,
// @RequestParam("image") MultipartFile image) {


// System.out.println("=================================");
// System.out.println("PROFILE PICTURE REQUEST RECEIVED");
// System.out.println("userId: " + userId);
// System.out.println("image: " + (image != null));

// if (image != null) {
//     System.out.println("filename: " + image.getOriginalFilename());
//     System.out.println("size: " + image.getSize());
//     System.out.println("content type: " + image.getContentType());
// }

// System.out.println("=================================");

// if (userId == null) {
//     return ResponseEntity.badRequest()
//             .body("userId is missing.");
// }

// if (image == null || image.isEmpty()) {
//     return ResponseEntity.badRequest()
//             .body("Image is missing or empty.");
// }

// try {

//     String url = profileService.updateProfilePicture(
//             userId,
//             image
//     );

//     System.out.println("ProfileService returned: " + url);

//     if (url.equals("User not found.")) {
//         return ResponseEntity.badRequest().body(url);
//     }

//     if (url.startsWith("Image upload failed")) {
//         return ResponseEntity.internalServerError().body(url);
//     }

//     Map<String, Object> response = new HashMap<>();

//     response.put(
//             "message",
//             "Profile picture updated successfully."
//     );

//     response.put(
//             "imageUrl",
//             url
//     );

//     return ResponseEntity.ok(response);

// } catch (Exception e) {

//     e.printStackTrace();

//     return ResponseEntity.internalServerError()
//             .body("Profile picture upload failed: " + e.getMessage());
// }


// }


    @PutMapping("/remove-picture")
    public ResponseEntity<String> removeProfilePicture(@RequestBody RemoveProfilePictureRequest request)
    {

        String result = profileService.removeProfilePicture(request);

        if(result.equals("User not found."))
        {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);

    }
}
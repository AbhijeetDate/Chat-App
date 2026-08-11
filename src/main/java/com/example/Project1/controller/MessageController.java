package com.example.Project1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project1.dto.MessageResponse;
import com.example.Project1.service.MessageService;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/{userId}/{otherUserId}")
    public ResponseEntity<List<MessageResponse>> getChatHistory(
            @PathVariable Integer userId,
            @PathVariable Integer otherUserId) {

        List<MessageResponse> messages =
                messageService.getChatHistory(
                        userId,
                        otherUserId
                );

        return ResponseEntity.ok(messages);
    }
}
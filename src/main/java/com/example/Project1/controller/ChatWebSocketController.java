package com.example.Project1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.Project1.dto.MessageResponse;
import com.example.Project1.dto.SendMessageRequest;
import com.example.Project1.service.MessageService;

@Controller
public class ChatWebSocketController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void sendMessage(SendMessageRequest request) {

        MessageResponse response =
                messageService.saveMessage(request);


        messagingTemplate.convertAndSend(
                "/topic/user/" + request.getReceiverId(),
                response
        );
    }
}
package com.example.Project1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project1.dto.AcceptConnectionRequest;
import com.example.Project1.dto.ConnectedUsersResponse;
import com.example.Project1.dto.SendConnectionRequest;
import com.example.Project1.entity.ConnectionRequest;
import com.example.Project1.service.ConnectionService;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ConnectionRequestController {
    
    @Autowired
    private ConnectionService connectionService;

    @PostMapping("/connections/send-connection-request")
    public String sendConnectionRequest(@RequestBody SendConnectionRequest request)
    {
        return connectionService.sendConnectionRequest(request);
    }

    @GetMapping("/connections/pending/{receiverId}")
    public List<ConnectionRequest> getPendingRequests(@PathVariable Integer receiverId)
    {
        return connectionService.getPendingRequests(receiverId);
    }

    @PutMapping("/connections/accept")
    public String acceptConnection(@RequestBody AcceptConnectionRequest request)
    {
        return connectionService.acceptRequest(request);
    }

    @GetMapping("/connections/get-connected-users/{userId}")
    public List<ConnectedUsersResponse> getConnectedUsers(@PathVariable Integer userId)
    {
        return connectionService.getConnectedUsers(userId);
    }
}

package com.example.Project1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Project1.dto.AcceptConnectionRequest;
import com.example.Project1.dto.ConnectedUsersResponse;
import com.example.Project1.dto.PendingConnectionResponse;
import com.example.Project1.dto.SendConnectionRequest;
import com.example.Project1.entity.ConnectionRequest;
import com.example.Project1.entity.Connections;
import com.example.Project1.entity.User;
import com.example.Project1.repository.ConnectionRepository;
import com.example.Project1.repository.ConnectionRequestRepository;
import com.example.Project1.repository.UserRepository;

@Service
public class ConnectionService {

    @Autowired
    private ConnectionRequestRepository connectionRequestRepository;
    @Autowired
    private ConnectionRepository connectionRepository;
    @Autowired
    private UserRepository userRepository;

    public String sendConnectionRequest(SendConnectionRequest request) 
    {
        if(request.getSenderId().equals(request.getReceiverId())) {
            return "Cannot send connection request to yourself.";
        }

        Optional<ConnectionRequest> existingRequest = connectionRequestRepository.findBySenderIdAndReceiverId(request.getSenderId(), request.getReceiverId());
        if(existingRequest.isPresent()) {
            return "Connection request already sent.";
        }

        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setSenderId(request.getSenderId());
        connectionRequest.setReceiverId(request.getReceiverId());
        connectionRequest.setStatus("PENDING");
        //connectionRequest.setCreatedAt(LocalDateTime.now());
        connectionRequestRepository.save(connectionRequest);

        return "Connection request sent.";
    }

    // public List<ConnectionRequest> getPendingRequests(Integer receiverId)
    // {
    //     return connectionRequestRepository.findByReceiverIdAndStatus(receiverId, "PENDING");
    // }

    public List<PendingConnectionResponse> getPendingRequests(Integer receiverId)
    {

        List<ConnectionRequest> requests =
            connectionRequestRepository.findByReceiverIdAndStatus(
                    receiverId,
                    "PENDING"
            );

        return requests.stream()
                .map(request -> {

                    User sender = userRepository
                        .findById(request.getSenderId())
                        .orElse(null);

                    String senderName =
                        sender != null
                                ? sender.getFullName()
                                : "Unknown User";

                    String profilePictureUrl =
                        sender != null
                                ? sender.getProfilePictureUrl()
                                : null;

                    return new PendingConnectionResponse(
                        request.getId(),
                        request.getSenderId(),
                        senderName,
                        profilePictureUrl,
                        request.getReceiverId(),
                        request.getStatus(),
                        request.getCreatedAt()
                    );
                })
                .toList();
    }

    public String acceptRequest(AcceptConnectionRequest request) 
    {
        Optional<ConnectionRequest> optionalRequest = connectionRequestRepository.findById(request.getRequestId());
        if(optionalRequest.isEmpty())
        {
            return "Connection request not found.";
        }

        ConnectionRequest connectionRequest = optionalRequest.get();
        connectionRequest.setStatus("ACCEPTED");
        connectionRequestRepository.save(connectionRequest);

        Connections connection = new Connections();
        connection.setUserId1(connectionRequest.getSenderId());
        connection.setUserId2(connectionRequest.getReceiverId());
        connectionRepository.save(connection);

        return "Connection request accepted.";
    }

    public List<ConnectedUsersResponse> getConnectedUsers(Integer userId) 
    {

        List<Connections> list1 = connectionRepository.findByUserId1(userId);
        List<Connections> list2 = connectionRepository.findByUserId2(userId);

        List<ConnectedUsersResponse> result = new ArrayList<>();

        for(Connections connection : list1) 
        {

            User user = userRepository.findById(connection.getUserId2()).get();

            result.add(new ConnectedUsersResponse(user.getId(), user.getFullName(), user.getEmail(), user.getPhone(), user.getProfilePictureUrl()));
        }

        for(Connections connection : list2) 
        {

            User user = userRepository.findById(connection.getUserId1()).get();

            result.add(new ConnectedUsersResponse(user.getId(), user.getFullName(), user.getEmail(), user.getPhone(), user.getProfilePictureUrl()));
        }
        return result;
    }
}

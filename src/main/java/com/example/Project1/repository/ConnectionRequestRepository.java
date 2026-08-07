package com.example.Project1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Project1.entity.ConnectionRequest;

public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest, Integer>{
    
    List<ConnectionRequest> findByReceiverIdAndStatus(Integer receiverId, String status);

    Optional<ConnectionRequest> findBySenderIdAndReceiverId(Integer senderId, Integer receiverId);
}

package com.example.Project1.dto;

import java.sql.Timestamp;

public class PendingConnectionResponse {

    private Integer id;
    private Integer senderId;
    private String senderName;
    private String senderProfilePictureUrl;
    private Integer receiverId;
    private String status;
    private Timestamp createdAt;

    public PendingConnectionResponse() {
    }

    public PendingConnectionResponse(
            Integer id,
            Integer senderId,
            String senderName,
            String senderProfilePictureUrl,
            Integer receiverId,
            String status,
            Timestamp createdAt) {

        this.id = id;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderProfilePictureUrl = senderProfilePictureUrl;
        this.receiverId = receiverId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderProfilePictureUrl() {
        return senderProfilePictureUrl;
    }

    public void setSenderProfilePictureUrl(String senderProfilePictureUrl) {
        this.senderProfilePictureUrl = senderProfilePictureUrl;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
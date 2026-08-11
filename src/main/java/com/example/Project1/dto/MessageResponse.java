package com.example.Project1.dto;

import java.sql.Timestamp;

public class MessageResponse {
    private Integer id;
    private Integer senderId;
    private Integer receiverId;
    private String message;
    private Timestamp timestamp;

    public MessageResponse() {}


    public MessageResponse(Integer id, Integer senderId, Integer receiverId, String message, Timestamp timestamp) {

        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = timestamp;
    }


    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSenderId() {
        return this.senderId;
    }
    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return this.receiverId;
    }
    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return this.timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }    
}

package com.example.Project1.dto;

public class SendMessageRequest {
    private Integer senderId;
    private Integer receiverId;
    private String message;

    public SendMessageRequest() {}

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

    
}

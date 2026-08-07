package com.example.Project1.dto;

public class SendConnectionRequest {
    private Integer senderId;
    private Integer receiverId;

    public SendConnectionRequest() {}

    public Integer getSenderId() {
        return senderId;
    }
    public Integer getReceiverId() {
        return receiverId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }
    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }
}

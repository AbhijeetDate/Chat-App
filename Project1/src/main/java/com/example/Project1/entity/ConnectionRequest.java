package com.example.Project1.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "connection_requests")
public class ConnectionRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "sender_id", nullable = false)
    private Integer senderId;

    @Column(name = "receiver_id", nullable = false)
    private Integer receiverId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @PrePersist
    protected void onCreate() 
    {
        createdAt = new Timestamp(System.currentTimeMillis());
    }

    public ConnectionRequest() {}

    // Getters
    public Integer getId() {
        return this.id;
    }
    public Integer getSenderId() {
        return this.senderId;
    }
    public Integer getReceiverId() {
        return this.receiverId;
    }
    public String getStatus() {
        return this.status;
    }
    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }
    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }
    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}

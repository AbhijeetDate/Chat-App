package com.example.Project1.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "messages")
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "sender_id", nullable = false)
    private Integer senderId;

    @Column(name = "receiver_id", nullable = false)
    private Integer receiverId;

    @Column(name = "message")
    private String message;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    public Message() {}

    public Integer getId()
    {
        return this.id;
    }

    public Integer getSenderId()
    {
        return this.senderId;
    }

    public Integer getReceiverId()
    {
        return this.receiverId;
    }

    public String getMessage()
    {
        return this.message;
    }

    public Timestamp getTimestamp()
    {
        return this.timestamp;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setSenderId(Integer senderId)
    {
        this.senderId = senderId;
    }

    public void setReceiverId(Integer receiverId)
    {
        this.receiverId = receiverId;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }

    public void setTimestamp(Timestamp timestamp)
    {
        this.timestamp = timestamp;
    }
}

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
@Table(name = "connections")
public class Connections {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user1_id", nullable = false)
    private Integer userId1;

    @Column(name = "user2_id", nullable = false)
    private Integer userId2;

    @Column(name = "connected_at")
    private Timestamp connectedAt;

    @PrePersist
    protected void onCreate() 
    {
        connectedAt = new Timestamp(System.currentTimeMillis());
    }

    public Connections() {}

    public Integer getId() {
        return id;
    }

    public Integer getUserId1() {
        return userId1;
    }

    public Integer getUserId2() {
        return userId2;
    }

    public Timestamp getConnectedAt() {
        return connectedAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId1(Integer userId1) {
        this.userId1 = userId1;
    }

    public void setUserId2(Integer userId2) {
        this.userId2 = userId2;
    }

    public void setConnectedAt(Timestamp connectedAt) {
        this.connectedAt = connectedAt;
    }
}

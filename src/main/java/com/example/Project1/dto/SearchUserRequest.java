package com.example.Project1.dto;

public class SearchUserRequest {
    
    private Integer currentUserId;
    private String keyword;

    public Integer getCurrentUserId() {
        return this.currentUserId;
    }

    public String getKeyword() {
        return this.keyword;
    }
}
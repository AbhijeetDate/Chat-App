package com.example.Project1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Project1.entity.Connections;

public interface ConnectionRepository extends JpaRepository<Connections, Integer>{
    
    List<Connections> findByUserId1(Integer user1Id);

    List<Connections> findByUserId2(Integer user2Id);
}

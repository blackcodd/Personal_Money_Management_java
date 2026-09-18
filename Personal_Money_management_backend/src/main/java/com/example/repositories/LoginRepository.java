package com.example.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.example.model.User;

@Service
@Repository
public interface LoginRepository extends JpaRepository<User, Long> {
    @Autowired

    User findByNameAndPassword(String username, String password);
}
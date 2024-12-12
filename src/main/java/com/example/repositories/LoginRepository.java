package com.example.repositories;

import com.example.Model.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<user, Long> {
    user findByNameAndPassword(String username, String password);
}